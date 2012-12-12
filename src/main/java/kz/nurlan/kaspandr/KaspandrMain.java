package kz.nurlan.kaspandr;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;

public class KaspandrMain implements Application {

	private KaspandrWindow kaspandrWindow = null;

	@Override
	public void resume() throws Exception {
		
	}

	@Override
	public boolean shutdown(boolean arg0) throws Exception {
		return false;
	}

	@Override
	public void startup(Display display, Map<String, String> properties)
			throws Exception {
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		kaspandrWindow = (KaspandrWindow) bxmlSerializer.readObject(KaspandrMain.class, "kaspandr_window.bxml");
		kaspandrWindow.open(display);
	}

	@Override
	public void suspend() throws Exception {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DesktopApplicationContext.main(KaspandrMain.class, args);
	}
}
