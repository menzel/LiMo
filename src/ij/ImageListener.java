package ij;

	/** Plugins that implement this interface are notified when
		an image window is opened, closed or updated. */
	public interface ImageListener {

	void imageOpened(ImagePlus imp);

	void imageClosed(ImagePlus imp);

	void imageUpdated(ImagePlus imp);

}
