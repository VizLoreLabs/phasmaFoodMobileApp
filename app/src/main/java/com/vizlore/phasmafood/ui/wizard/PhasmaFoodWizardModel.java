/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vizlore.phasmafood.ui.wizard;

import android.content.Context;

import wizardpager.model.AbstractWizardModel;
import wizardpager.model.BranchPage;
import wizardpager.model.NumberPage;
import wizardpager.model.PageList;
import wizardpager.model.SingleFixedChoicePage;
import wizardpager.model.TextPage;

public class PhasmaFoodWizardModel extends AbstractWizardModel {

	public PhasmaFoodWizardModel(Context context) {
		super(context);
	}

	@Override
	protected PageList onNewRootPageList() {
		return new PageList(
			new BranchPage(this, "Use cases")
				.addBranch("1 Mycotoxin detection",
					new BranchPage(this, "1 Mycotoxin detection ")
						.addBranch("1.1 Maize flour",
							new SingleFixedChoicePage(this, "1.1.1 Granularity").setChoices("Low", "Medium", "High").setRequired(true),
							new SingleFixedChoicePage(this, "1.1.2 Mycotoxins").setChoices("AF B1", "Total AFs", "DON").setRequired(true))
						.addBranch("1.2 Skimmed milk powder",
							new SingleFixedChoicePage(this, "1.2.1 Granularity").setChoices("Low", "Medium", "High").setRequired(true),
							new SingleFixedChoicePage(this, "1.2.2 Mycotoxins").setChoices("AF B1", "Total AFs", "DON").setRequired(true))
						.addBranch("1.3 Paprika powder")
						.addBranch("1.4 Almond",
							new SingleFixedChoicePage(this, "1.3.1 Granularity").setChoices("Low", "Medium", "High").setRequired(true),
							new SingleFixedChoicePage(this, "1.3.2 Mycotoxins").setChoices("AF B1", "Total AFs", "DON").setRequired(true))
						.addBranch("1.5 Peanuts",
							new SingleFixedChoicePage(this, "1.4.1 Granularity").setChoices("Low", "Medium", "High").setRequired(true),
							new SingleFixedChoicePage(this, "1.4.2 Mycotoxins").setChoices("AF B1", "Total AFs", "DON")).setRequired(true)
						.setRequired(true))

				.addBranch("2 Food spoilage",
					new BranchPage(this, "Step 2")
						.addBranch("Food spoilage", new BranchPage(this, "Food type")
							.addBranch("Meat", new BranchPage(this, "Choose food spoilage")
								.addBranch("Exposure time", new NumberPage(this, "Exposure time"))
								.addBranch("Sample temperature", new NumberPage(this, "Sample temperature"))
								.addBranch("Packaging", new TextPage(this, "Packaging"))
								.addBranch("Meat type", new TextPage(this, "Meat type"))
								.addBranch("Sample state", new TextPage(this, "Sample state"))
							)
							.addBranch("Fish", new BranchPage(this, "Choose")
								.addBranch("Exposure time", new NumberPage(this, "Exposure time"))
								.addBranch("Sample temperature", new NumberPage(this, "Sample temperature"))
								.addBranch("Packaging", new TextPage(this, "Packaging"))
								.addBranch("Meat type", new TextPage(this, "Meat type"))
								.addBranch("Sample state", new TextPage(this, "Sample state"))
							)
							.addBranch("Fruits and vegetables", new BranchPage(this, "Choose")
								.addBranch("Ready-to-eat rocket", new BranchPage(this, "Ready-to-eat rocket")
									.addBranch("Exposure time", new NumberPage(this, "Exposure time"))
									.addBranch("Sample temperature", new NumberPage(this, "Sample temperature"))
								)
								.addBranch("Ready-to-eat pineapple", new BranchPage(this, "Ready-to-eat pineapple")
									.addBranch("Exposure time", new NumberPage(this, "Exposure time"))
									.addBranch("Sample temperature", new NumberPage(this, "Sample temperature"))
								)
							).setRequired(true))

						.addBranch("Shelf Life estimation", new BranchPage(this, "Food type")
							.addBranch("Meat", new BranchPage(this, "Choose")
								.addBranch("Exposure time", new NumberPage(this, "Exposure time"))
								.addBranch("Sample temperature", new NumberPage(this, "Sample temperature"))
								.addBranch("Packaging", new TextPage(this, "Packaging"))
								.addBranch("Meat type", new TextPage(this, "Meat type"))
								.addBranch("Sample state", new TextPage(this, "Sample state"))
							)
							.addBranch("Fish", new BranchPage(this, "Choose")
								.addBranch("Exposure time", new NumberPage(this, "Exposure time"))
								.addBranch("Sample temperature", new NumberPage(this, "Sample temperature"))
								.addBranch("Packaging", new TextPage(this, "Packaging"))
								.addBranch("Meat type", new TextPage(this, "Meat type"))
								.addBranch("Sample state", new TextPage(this, "Sample state"))
							)
							.addBranch("Fruits and vegetables", new BranchPage(this, "Choose")
								.addBranch("Ready-to-eat rocket", new BranchPage(this, "Ready-to-eat rocket")
									.addBranch("Exposure time", new NumberPage(this, "Exposure time"))
									.addBranch("Sample temperature", new NumberPage(this, "Sample temperature"))
								)
								.addBranch("Ready-to-eat pineapple", new BranchPage(this, "Ready-to-eat pineapple")
									.addBranch("Exposure time", new NumberPage(this, "Exposure time"))
									.addBranch("Sample temperature", new NumberPage(this, "Sample temperature"))
								)
							)))

				.addBranch("3 Food adulteration", new BranchPage(this, "Food adulteration")
					.addBranch("Alcoholic beverages", new BranchPage(this, "Alcoholic beverages")
						.addBranch("Spirits",
							new SingleFixedChoicePage(this, "Spirits")
								.setChoices("Dilution", "Technical alcohol", "Counterfeit").setRequired(true))
						.addBranch("Wines and beers",
							new SingleFixedChoicePage(this, "Wines and beers")
								.setChoices("Sugar", "Acid", "Alcohol (alcohol by volume)").setRequired(true))
					)
					.addBranch("Edible oils", new BranchPage(this, "Edible oils")
						.addBranch("Olive oil",
							new SingleFixedChoicePage(this, "Spirits")
								.setChoices("Dilution", "Counterfeit").setRequired(true))
						.addBranch("SunfLower oil",
							new SingleFixedChoicePage(this, "Wines and beers")
								.setChoices("Dilution", "Counterfeit").setRequired(true))
					)
					.addBranch("Skimmed milk powder", new SingleFixedChoicePage(this, "Skimmed milk powder")
						.setChoices("Milk powder dilution", "Nitrogen enhancers").setRequired(true))

					.addBranch("Meat", new SingleFixedChoicePage(this, "Meat")
						.setChoices("Adulteration with unspecified meat species", "Adulteration with Low value additives").setRequired(true))
				));
	}
}
