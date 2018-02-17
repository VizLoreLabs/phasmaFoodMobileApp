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
				.addBranch("Mycotoxin detection",
					new BranchPage(this, "Mycotoxin detection")
						.addBranch("Maize flour",
							new SingleFixedChoicePage(this, "1!Granularity").setChoices("Low", "Medium", "High").setRequired(true),
							new SingleFixedChoicePage(this, "1!Mycotoxins").setChoices("AF B1", "Total AFs", "DON").setRequired(true))
						.addBranch("Skimmed milk powder",
							new SingleFixedChoicePage(this, "2!Granularity").setChoices("Low", "Medium", "High").setRequired(true),
							new SingleFixedChoicePage(this, "2!Mycotoxins").setChoices("AF B1", "Total AFs", "DON").setRequired(true))
						.addBranch("Paprika powder")
						.addBranch("Almond",
							new SingleFixedChoicePage(this, "3!Granularity").setChoices("Low", "Medium", "High").setRequired(true),
							new SingleFixedChoicePage(this, "3!Mycotoxins").setChoices("AF B1", "Total AFs", "DON").setRequired(true))
						.addBranch("Peanuts",
							new SingleFixedChoicePage(this, "4!Granularity").setChoices("Low", "Medium", "High").setRequired(true),
							new SingleFixedChoicePage(this, "4!Mycotoxins").setChoices("AF B1", "Total AFs", "DON")).setRequired(true)
						.setRequired(true))

				.addBranch("Food spoilage",
					new BranchPage(this, "Food spoilage")
						.addBranch("Food spoilage", new BranchPage(this, "1!Food spoilage")
							.addBranch("Meat", new BranchPage(this, "1!Choose meat spoilage")
								.addBranch("Exposure time", new NumberPage(this, "1!Exposure time"))
								.addBranch("Sample temperature", new NumberPage(this, "1!Sample temperature"))
								.addBranch("Packaging", new TextPage(this, "1!Packaging"))
								.addBranch("Meat type", new TextPage(this, "1!Meat type"))
								.addBranch("Sample state", new TextPage(this, "1!Sample state"))
							)
							.addBranch("Fish", new BranchPage(this, "2!Fish")
								.addBranch("Exposure time", new NumberPage(this, "2!Exposure time"))
								.addBranch("Sample temperature", new NumberPage(this, "2!Sample temperature"))
								.addBranch("Packaging", new TextPage(this, "2!Packaging"))
								.addBranch("Meat type", new TextPage(this, "2!Meat type"))
								.addBranch("Sample state", new TextPage(this, "2!Sample state"))
							)
							.addBranch("Fruits and vegetables", new BranchPage(this, "3!Fruits and vegetables")
								.addBranch("Ready-to-eat rocket", new BranchPage(this, "3!Ready-to-eat rocket")
									.addBranch("Exposure time", new NumberPage(this, "3!Exposure time"))
									.addBranch("Sample temperature", new NumberPage(this, "3!Sample temperature"))
								)
								.addBranch("Ready-to-eat pineapple", new BranchPage(this, "4!Ready-to-eat pineapple")
									.addBranch("Exposure time", new NumberPage(this, "4!Exposure time"))
									.addBranch("Sample temperature", new NumberPage(this, "4!Sample temperature"))
								)
							).setRequired(true))

						.addBranch("Shelf Life estimation", new BranchPage(this, "Shelf Life estimation")
							.addBranch("Meat", new BranchPage(this, "5!Meat")
								.addBranch("Exposure time", new NumberPage(this, "5!Exposure time"))
								.addBranch("Sample temperature", new NumberPage(this, "5!Sample temperature"))
								.addBranch("Packaging", new TextPage(this, "5!Packaging"))
								.addBranch("Meat type", new TextPage(this, "5!Meat type"))
								.addBranch("Sample state", new TextPage(this, "5!Sample state"))
							)
							.addBranch("Fish", new BranchPage(this, "6!Fish")
								.addBranch("Exposure time", new NumberPage(this, "6!Exposure time"))
								.addBranch("Sample temperature", new NumberPage(this, "6!Sample temperature"))
								.addBranch("Packaging", new TextPage(this, "6!Packaging"))
								.addBranch("Meat type", new TextPage(this, "6!Meat type"))
								.addBranch("Sample state", new TextPage(this, "6!Sample state"))
							)
							.addBranch("Fruits and vegetables", new BranchPage(this, "Fruits and vegetables")
								.addBranch("Ready-to-eat rocket", new BranchPage(this, "7!Ready-to-eat rocket")
									.addBranch("Exposure time", new NumberPage(this, "7!Exposure time"))
									.addBranch("Sample temperature", new NumberPage(this, "7!Sample temperature"))
								)
								.addBranch("Ready-to-eat pineapple", new BranchPage(this, "Ready-to-eat pineapple")
									.addBranch("Exposure time", new NumberPage(this, "8!Exposure time"))
									.addBranch("Sample temperature", new NumberPage(this, "8!Sample temperature"))
								)
							)))

				.addBranch("Food adulteration", new BranchPage(this, "Food adulteration")
					.addBranch("Alcoholic beverages", new BranchPage(this, "Alcoholic beverages")
						.addBranch("Spirits",
							new SingleFixedChoicePage(this, "9!Spirits")
								.setChoices("Dilution", "Technical alcohol", "Counterfeit").setRequired(true))
						.addBranch("Wines and beers",
							new SingleFixedChoicePage(this, "9!Wines and beers")
								.setChoices("Sugar", "Acid", "Alcohol (alcohol by volume)").setRequired(true))
					)
					.addBranch("Edible oils", new BranchPage(this, "Edible oils")
						.addBranch("Olive oil",
							new SingleFixedChoicePage(this, "10!Spirits")
								.setChoices("Dilution", "Counterfeit").setRequired(true))
						.addBranch("SunfLower oil",
							new SingleFixedChoicePage(this, "10!Wines and beers")
								.setChoices("Dilution", "Counterfeit").setRequired(true))
					)
					.addBranch("Skimmed milk powder",
						new SingleFixedChoicePage(this, "11!Skimmed milk powder")
							.setChoices("Milk powder dilution", "Nitrogen enhancers").setRequired(true))

					.addBranch("Meat",
						new SingleFixedChoicePage(this, "12!Meat")
							.setChoices("Adulteration with unspecified meat species", "Adulteration with Low value additives")
							.setRequired(true))
				));
	}
}
