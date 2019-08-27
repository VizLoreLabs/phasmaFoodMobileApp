package com.vizlore.phasmafood.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class CommunicationController {

	private static final String TAG = "SMEDIC CC";

	private static final String APP_NAME = "PhasmaFoodApp";
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private final BluetoothAdapter bluetoothAdapter;
	private final Handler handler;
	private AcceptThread acceptThread;
	private ConnectThread connectThread;
	private ReadWriteThread connectedThread;
	private int state;

	static final int STATE_NONE = 0;
	static final int STATE_LISTEN = 1;
	static final int STATE_CONNECTING = 2;
	static final int STATE_CONNECTED = 3;

	public CommunicationController(Handler handler) {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		state = STATE_NONE;

		this.handler = handler;
	}

	// Set the current state of the connection
	private synchronized void setState(int state) {
		this.state = state;

		handler.obtainMessage(BluetoothService.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
	}

	// get current connection state
	public synchronized int getState() {
		return state;
	}

	// start service
	public synchronized void start() {
		// Cancel any thread
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}

		// Cancel any running thread
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		setState(STATE_LISTEN);
		if (acceptThread == null) {
			acceptThread = new AcceptThread();
			acceptThread.start();
		}
	}

	// initiate connection to remote device
	public synchronized void connect(BluetoothDevice device) {
		// Cancel any thread
		if (state == STATE_CONNECTING) {
			if (connectThread != null) {
				connectThread.cancel();
				connectThread = null;
			}
		}

		// Cancel running thread
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		// Start the thread to connect with the given device
		connectThread = new ConnectThread(device);
		connectThread.start();
		setState(STATE_CONNECTING);
	}

	// manage Bluetooth connection
	public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
		// Cancel the thread
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}

		// Cancel running thread
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		if (acceptThread != null) {
			acceptThread.cancel();
			acceptThread = null;
		}

		// Start the thread to manage the connection and perform transmissions
		connectedThread = new ReadWriteThread(socket);
		connectedThread.start();

		// Send the name of the connected device back
		final Message msg = handler.obtainMessage(BluetoothService.MESSAGE_DEVICE_OBJECT);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(BluetoothService.DEVICE_OBJECT, device);
		msg.setData(bundle);
		handler.sendMessage(msg);

		setState(STATE_CONNECTED);
	}

	// stop all threads
	public synchronized void stop() {
		Log.d(TAG, "stop: ");
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}

		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		if (acceptThread != null) {
			acceptThread.cancel();
			acceptThread = null;
		}
		setState(STATE_NONE);

	}

	public void write(byte[] out) {
		ReadWriteThread r;
		synchronized (this) {
			if (state != STATE_CONNECTED)
				return;
			r = connectedThread;
		}
		r.write(out);
	}

	private void connectionFailed() {
		final Message msg = handler.obtainMessage(BluetoothService.MESSAGE_TOAST);
		final Bundle bundle = new Bundle();
		bundle.putString("toast", "Unable to connect device");
		msg.setData(bundle);
		handler.sendMessage(msg);
		//CommunicationController.this.stop();
		// Start the service over to restart listening mode
		CommunicationController.this.start();
	}

	private void connectionLost() {
		final Message msg = handler.obtainMessage(BluetoothService.MESSAGE_TOAST);
		final Bundle bundle = new Bundle();
		bundle.putString("toast", "Device connection was lost");
		msg.setData(bundle);
		handler.sendMessage(msg);
		//CommunicationController.this.stop();
		// Start the service over to restart listening mode
		CommunicationController.this.start();
	}

	// runs while listening for incoming connections
	private class AcceptThread extends Thread {
		private final BluetoothServerSocket serverSocket;

		public AcceptThread() {
			BluetoothServerSocket tmp = null;
			try {
				tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, MY_UUID);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			serverSocket = tmp;
		}

		public void run() {
			setName("AcceptThread");
			BluetoothSocket socket;
			while (state != STATE_CONNECTED) {
				try {
					socket = serverSocket.accept();
				} catch (IOException e) {
					break;
				}

				// If a connection was accepted
				if (socket != null) {
					synchronized (CommunicationController.this) {
						switch (state) {
							case STATE_LISTEN:
							case STATE_CONNECTING:
								// start the connected thread.
								connected(socket, socket.getRemoteDevice());
								break;
							case STATE_NONE:
							case STATE_CONNECTED:
								// Either not ready or already connected. Terminate
								// new socket.
								try {
									socket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
								break;
						}
					}
				}
			}
		}

		public void cancel() {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// runs while attempting to make an outgoing connection
	private class ConnectThread extends Thread {
		private final BluetoothSocket socket;
		private final BluetoothDevice device;

		public ConnectThread(BluetoothDevice device) {
			this.device = device;
			BluetoothSocket tmp = null;
			try {
				tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				e.printStackTrace();
			}
			socket = tmp;
		}

		public void run() {
			setName("ConnectThread");

			// Always cancel discovery because it will slow down a connection
			bluetoothAdapter.cancelDiscovery();

			// Make a connection to the BluetoothSocket
			try {
				socket.connect();
			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				connectionFailed();
				return;
			}

			// Reset the ConnectThread because we're done
			synchronized (CommunicationController.this) {
				connectThread = null;
			}

			// Start the connected thread
			connected(socket, device);
		}

		public void cancel() {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// runs during a connection with a remote device
	private class ReadWriteThread extends Thread {

		private final BluetoothSocket bluetoothSocket;
		private final InputStream inputStream;
		private final OutputStream outputStream;

		private String status;
		private int dataSize;
		private String type;
		private int state;
		private String dataType;

		//debug
		//private int fullSize = 0;
		//private String outputFileName;


		public ReadWriteThread(BluetoothSocket socket) {
			this.bluetoothSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}

			inputStream = tmpIn;
			outputStream = tmpOut;

		}

		public void run() {
			byte[] buffer = new byte[512];
			int bytes;
			int state = 0;

			int dataFlag = 0; // TODO: 10/16/18 fix
			int endFlag = 6; // TODO: 10/16/18 fix

			int stage = 0;
			final int[] stages = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			// Keep listening to the InputStream
			while (true) {
				try {
//					try {
//						sleep(100);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					// Read from the InputStream
					bytes = inputStream.read(buffer);
					final String readMessage = new String(buffer, 0, bytes);

					Log.d(TAG, "run: READ MESSAGE size:" + readMessage.length() + ", content:" + readMessage);
					//saveToFile(readMessage);

					if (readMessage.equals("Cancelled")) {
						handler.obtainMessage(BluetoothService.MESSAGE_READ, outputStream.toByteArray().length,
							7, outputStream.toByteArray()).sendToTarget();
						state = 0;
						outputStream = new ByteArrayOutputStream();
						stage = 0;
						endFlag = 6;
						continue;
					}
					if (state == 0) {
						if (decode(readMessage)) {
							switch (type) {
								case "00": // Sample Measurement Started
								case "01": // VIS Measurement Captured
								case "02": // NIR Measurement Captured
								case "03": // FLUO Measurement Captured
									//case "05": // White Reference Captured
									state = 0;
									handler.obtainMessage(BluetoothService.MESSAGE_READ, outputStream.toByteArray().length,
										3, type).sendToTarget();
									break;
								case "04": // data with specific “size” transmission started.
									state = 1;

									if (readMessage.contains("}}{")) {
										dataFlag = 0;
										endFlag = 6;
										final String newJsonCut = readMessage.substring(readMessage.indexOf("}}{") + 2);
										outputStream.write(newJsonCut.getBytes(), 0, newJsonCut.getBytes().length);
										double percentage = ((double) outputStream.size() / newJsonCut.getBytes().length) * 100;

										boolean flag = false;
										for (int stage1 : stages) {
											if (percentage >= stage1) {
												if (stage < stage1) {
													stage = stage1;
													flag = true;
												}
											}
										}

										if (flag) {
											handler.obtainMessage(BluetoothService.MESSAGE_READ, outputStream.toByteArray().length,
												dataFlag, stage).sendToTarget();
										}
									}

									break;
							}
						}
					} else if (state == 1) {
						if (dataType.equals("Image")) {
							dataFlag = 1;
							endFlag = 5;
						} else if (dataType.equals("data")) {
							dataFlag = 0;
							endFlag = 6;
						}

						if (readMessage.equals("End of Response")) {
							//outputFileName = null;

							state = 0; //reset state
							handler.obtainMessage(BluetoothService.MESSAGE_READ, outputStream.toByteArray().length,
								endFlag, outputStream.toByteArray()).sendToTarget();
							outputStream = new ByteArrayOutputStream();
							stage = 0;
							//fullSize = 0;

						} else {
							outputStream.write(buffer, 0, bytes);
							double percentage = ((double) outputStream.size() / dataSize) * 100;

							boolean flag = false;
							for (int stage1 : stages) {
								if (percentage >= stage1) {
									if (stage < stage1) {
										stage = stage1;
										flag = true;
									}
								}
							}

							if (flag) {
								handler.obtainMessage(BluetoothService.MESSAGE_READ, outputStream.toByteArray().length,
									dataFlag, stage).sendToTarget();
							}

							if (readMessage.contains("End of Response")) {
								Log.d(TAG, "run: HANDLING ANOTHER EDGE CASE...");
								state = 0; //reset state
								handler.obtainMessage(BluetoothService.MESSAGE_READ, outputStream.toByteArray().length,
									endFlag, outputStream.toByteArray()).sendToTarget();
								outputStream = new ByteArrayOutputStream();
								stage = 0;
							}
						}
					}
					// Send the obtained bytes to the UI Activity
					//  handler.obtainMessage(MainActivity.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
				} catch (IOException e) {
					connectionLost();
					break;
				}
			}
		}

		// write to OutputStream
		private void write(byte[] buffer) {
			try {
				outputStream.write(buffer);
				handler.obtainMessage(BluetoothService.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void cancel() {
			try {
				bluetoothSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private boolean decode(final String jsonString) {
			String json = jsonString;
			if (jsonString.contains("}}{")) {
				json = jsonString.substring(0, jsonString.indexOf("}}{") + 2);
			}

			try {
				final JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
				final JSONObject response = jsonObject.getJSONObject("response");
				status = response.getString("status");
				type = response.getString("type");
				dataSize = response.getInt("size");
				dataType = response.getString("datatype");
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}

		private boolean decodeCancelMessage(final String json) {
			try {
				final JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
				final JSONObject response = jsonObject.getJSONObject("response");
				status = response.getString("status");
				type = response.getString("type");
				dataSize = response.getInt("size");
				dataType = response.getString("datatype");

				Log.d(TAG, "decode: status: " + status + ", type: " + type + ", size: " + dataSize + ", datatype: " + dataType);

				return true;
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}

		//DEBUG
//		private void saveToFilee(String data) {
//			if (outputFileName == null) {
//				outputFileName = new SimpleDateFormat("dd_MM_yyyy_hh-mm-ss").format(new Date());
//			}
//			File file = new File("/sdcard/phasma_test_" + outputFileName + ".txt");
//			if (!file.exists()) {
//				try {
//					file.createNewFile();
//				} catch (IOException ioe) {
//					ioe.printStackTrace();
//				}
//			}
//			try {
//				FileOutputStream fileOutputStream = new FileOutputStream(file, true);
//				OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
//				writer.append(data);
//				writer.close();
//				fileOutputStream.close();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

	public int getFullSize() {
//		if (connectedThread != null) {
//			return connectedThread.fullSize;
//		}
		return 0;
	}
}

