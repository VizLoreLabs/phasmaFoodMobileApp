package com.vizlore.phasmafood.ui.wizard;

import android.content.Context;

import com.vizlore.phasmafood.utils.Constants;

import wizardpager.model.AbstractWizardModel;
import wizardpager.model.BranchPage;
import wizardpager.model.MultipleFixedChoicePage;
import wizardpager.model.NumberPage;
import wizardpager.model.PageList;
import wizardpager.model.SingleFixedChoicePage;

public class PhasmaFoodWizardModel extends AbstractWizardModel {

	public PhasmaFoodWizardModel(Context context) {
		super(context);
	}

	@Override
	protected PageList onNewRootPageList() {
		return new PageList(
			new BranchPage(this, Constants.USE_CASE_KEY)
				.addBranch(Constants.USE_CASE_1,
					new BranchPage(this, "1!Food type")
						.addBranch("Maize flour",
							new SingleFixedChoicePage(this, "1!Granularity").setChoices("Low", "Medium", "High").setRequired(true),
							new SingleFixedChoicePage(this, "1!Mycotoxins").setChoices("AF B1", "Total AFs", "DON").setRequired(true))
						.addBranch("Wheat",
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

				.addBranch(Constants.USE_CASE_2,
					new BranchPage(this, "2!Food type")
						.addBranch("Minced pork",
							new SingleFixedChoicePage(this, "1!Packaging")
								.setChoices("Foil", "No package"),
							new NumberPage(this, "1!Exposure time"),
							new NumberPage(this, "1!Sample temperature"))
						.addBranch("Fish",
							//new SingleFixedChoicePage(this, "2!Sample state")
							//	.setChoices("Fillet", "Whole fish"),
							new SingleFixedChoicePage(this, "2!Packaging")
								.setChoices("Foil", "No package"),
							new NumberPage(this, "2!Exposure time"),
							new NumberPage(this, "2!Sample temperature"))
						.addBranch("Ready to eat pineapple",
							new NumberPage(this, "3!Exposure time"),
							new NumberPage(this, "3!Sample temperature"))
						.addBranch("Ready to eat baby spinach",
							new NumberPage(this, "4!Exposure time"),
							new NumberPage(this, "4!Sample temperature"))
						.addBranch("Ready to eat rocket salad",
							new NumberPage(this, "5!Exposure time"),
							new NumberPage(this, "5!Sample temperature"))

//						.addBranch("Meat",
//							new SingleFixedChoicePage(this, "1!Meat type")
//								.setChoices("Chicken", "Pork", "Beef"),
//							new SingleFixedChoicePage(this, "1!Sample state")
//								.setChoices("Minced", "Whole piece"),
//							new SingleFixedChoicePage(this, "1!Packaging")
//								.setChoices("Foil", "No package"),
//							new NumberPage(this, "1!Exposure time"),
//							new NumberPage(this, "1!Sample temperature"))
//						.addBranch("Fish",
//							new SingleFixedChoicePage(this, "2!Fish type")
//								.setChoices("Gilthead Seabream"), // TODO: 2/19/18 add more options later
//							new SingleFixedChoicePage(this, "2!Sample state")
//								.setChoices("Fillet", "Whole fish"),
//							new SingleFixedChoicePage(this, "2!Packaging")
//								.setChoices("Foil", "No package"),
//							new NumberPage(this, "2!Exposure time"),
//							new NumberPage(this, "2!Sample temperature"))
//						.addBranch("Fruits and vegetables", new BranchPage(this, "3!Fruits and vegetables")
//							.addBranch("Ready-to-eat rocket",
//								new NumberPage(this, "3!Exposure time"),
//								new NumberPage(this, "3!Sample temperature"))
//							.addBranch("Ready-to-eat pineapple",
//								new NumberPage(this, "4!Exposure time"),
//								new NumberPage(this, "4!Sample temperature"))
//							.addBranch("Baby spinach",
//								new NumberPage(this, "5!Exposure time"),
//								new NumberPage(this, "5!Sample temperature"))
//							.addBranch("Pineapple",
//								new NumberPage(this, "6!Exposure time"),
//								new NumberPage(this, "6!Sample temperature"))
//							.addBranch("Rocket salad",
//								new NumberPage(this, "7!Exposure time"),
//								new NumberPage(this, "7!Sample temperature"))
//						).setRequired(true)
				)
				.addBranch(Constants.USE_CASE_3,
					new BranchPage(this, "3!Food type")
						.addBranch("Alcoholic beverages", new BranchPage(this, "Alcoholic beverages")
							.addBranch("Spirits",
								new SingleFixedChoicePage(this, "9!Analysis type")
									.setChoices("Dilution", "Technical alcohol", "Counterfeit").setRequired(true))
							.addBranch("Wines and beers",
								new SingleFixedChoicePage(this, "10!Analysis type")
									.setChoices("Sugar", "Acid", "Alcohol (alcohol by volume)").setRequired(true))
						)
						.addBranch("Edible oils", new BranchPage(this, "Edible oils")
							.addBranch("Olive oil",
								new SingleFixedChoicePage(this, "11!Analysis type")
									.setChoices("Dilution", "Counterfeit").setRequired(true))
							.addBranch("SunfLower oil",
								new SingleFixedChoicePage(this, "12!Analysis type")
									.setChoices("Dilution", "Counterfeit").setRequired(true))
						)
						.addBranch("Skimmed milk powder",
							new SingleFixedChoicePage(this, "13!Analysis type")
								.setChoices("Milk powder dilution", "Nitrogen enhancers").setRequired(true))

						.addBranch("Meat",
							new SingleFixedChoicePage(this, "14!Analysis type")
								.setChoices("Adulteration with unspecified meat species", "Adulteration with Low value additives")
								.setRequired(true))
				)
				.addBranch(Constants.USE_CASE_WHITE_REF,
					new SingleFixedChoicePage(this, "type")
						.setChoices("NEW", "EXISTING")
						.setRequired(true))
				.addBranch(Constants.USE_CASE_TEST,
					new MultipleFixedChoicePage(this, Constants.USE_CASE_TEST)
						.setChoices(Constants.USE_CASE_TEST_VIS, Constants.USE_CASE_TEST_NIR,
							Constants.USE_CASE_TEST_FLUO, Constants.USE_CASE_TEST_CAMERA)
						.setRequired(true)));
	}
}
