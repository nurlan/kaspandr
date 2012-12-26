/*Copyright © 2012 Nurlan Rakhimzhanov
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

package kz.nurlan.kaspandr;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;

public class KaspandrMain implements Application {

	private static final int MEGABYTE = (1024*1024);

	private KaspandrWindow kaspandrWindow = null;

	@Override
	public void resume() throws Exception {
		
	}

	@Override
	public boolean shutdown(boolean arg0) throws Exception {
		return false;
	}

	@Override
	public void startup(Display display, Map<String, String> properties) {
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
		
		long initMemory = heapUsage.getInit() / MEGABYTE;
		long commitMemory = heapUsage.getCommitted() / MEGABYTE;
		long maxMemory = heapUsage.getMax() / MEGABYTE;
        long usedMemory = heapUsage.getUsed() / MEGABYTE;
        
        System.out.println("Memory Use  : " + usedMemory + "MB");
        System.out.println("Memory Init : " + initMemory + "MB");
        System.out.println("Memory Committed : " + commitMemory + "MB");
        System.out.println("Memory Max  : " + maxMemory + "MB");
        
		try {
			kaspandrWindow = (KaspandrWindow) bxmlSerializer.readObject(KaspandrMain.class, "kaspandr_window.bxml");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		
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
