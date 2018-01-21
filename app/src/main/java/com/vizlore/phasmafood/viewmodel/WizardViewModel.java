package com.vizlore.phasmafood.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.vizlore.phasmafood.MyApplication;
import com.vizlore.phasmafood.model.Subcase;
import com.vizlore.phasmafood.model.Subproblem;
import com.vizlore.phasmafood.model.UseCase;
import com.vizlore.phasmafood.utils.JsonFileLoader;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by smedic on 1/21/18.
 */

public class WizardViewModel extends ViewModel {

	private static final String TAG = "SMEDIC";
	private List<UseCase> useCases;

	private int selection1 = -1;
	private int selection2 = -1;
	private int selection3 = -1;

	@Inject
	Gson gson;

	public WizardViewModel() {

		MyApplication.getComponent().inject(this);
		String json = new JsonFileLoader().fromAsset("assets/wizard.json");
		useCases = Arrays.asList(gson.fromJson(json, UseCase[].class));
	}

	public List<UseCase> getUseCases() {
		return useCases;
	}

	public List<Subcase> getSubcases() {
		if (selection1 != -1 && useCases.get(selection1) != null) {
			return useCases.get(selection1).getSubcases();
		}
		return null;
	}

	public List<Subproblem> getSubproblems() {
		if (selection2 != -1 && getSubcases().get(selection2) != null) {
			return getSubcases().get(selection2).getSubproblems();
		}
		return null;
	}

	public int getSelection1() {
		return selection1;
	}

	public String getSelection1Text() {
		return useCases.get(selection1).getName();
	}

	public void setSelection1(int selection1) {
		if (this.selection1 != selection1) {
			this.selection2 = -1;
			this.selection3 = -1;
		}
		this.selection1 = selection1;
	}

	public int getSelection2() {
		return selection2;
	}

	public String getSelection2Text() {
		return getSubcases().get(selection2).getName();
	}

	public void setSelection2(int selection2) {
		if (this.selection2 != selection2) {
			this.selection3 = -1;
		}
		this.selection2 = selection2;
	}

	public int getSelection3() {
		return selection3;
	}

	public String getSelection3Text() {
		return getSubproblems().get(selection3).getName();
	}

	public void setSelection3(int selection3) {
		this.selection3 = selection3;
	}
}
