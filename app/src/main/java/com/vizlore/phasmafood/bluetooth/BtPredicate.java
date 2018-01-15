package com.vizlore.phasmafood.bluetooth;

import android.content.BroadcastReceiver;

import io.reactivex.functions.Predicate;

/**
 * Class that contains predicates for filtering bluetooth states, actions and other indicators
 * received from {@link BroadcastReceiver}.
 */
public class BtPredicate {

	/**
	 * Function, which checks if current object equals single argument or one of many
	 * arguments. It can be used inside filter(...) method from RxJava.
	 *
	 * @param arguments many arguments or single argument
	 * @return Predicate function
	 */
	public static <T> Predicate<T> in(final T... arguments) {
		return object -> {
			for (T t : arguments) {
				if (t.equals(object)) {
					return true;
				}
			}
			return false;
		};
	}
}