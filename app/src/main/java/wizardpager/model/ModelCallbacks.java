package wizardpager.model;

/**
 * Callback interface connecting {@link Page}, {@link AbstractWizardModel}, and model container
 * objects
 */
public interface ModelCallbacks {
	void onPageDataChanged(Page page);

	void onPageTreeChanged();
}
