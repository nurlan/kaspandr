package kz.nurlan.kaspandr;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class KaspandrWindow extends Window implements Bindable {
    
	@BXML private PushButton jsonMergeButton = null;
	@BXML private PushButton checkGroupButton1 = null;
	@BXML private PushButton checkGroupButton2 = null;
	@BXML private PushButton checkGroupButton3 = null;
    @BXML private TextInput firstJson = null;
    @BXML private TextInput secondJson = null;
    @BXML private Label finalLessonSequence = null;
    @BXML private Label checkGroupText1 = null;
    @BXML private Label checkGroupText2 = null;
    @BXML private Label checkGroupText3 = null;
    @BXML private TextArea finalJsonText = null;

    private ObjectMapper mapper;
    
    @Override
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
    	mapper = new ObjectMapper();
        jsonMergeButton.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {

				try {
					ObjectNode rootNode1 = mapper.readValue("{\"lessons\":["+firstJson.getText().replaceAll("'", "\"")+"]}", ObjectNode.class);
					JsonNode lessonsNode1 = rootNode1.get("lessons");

					ObjectNode rootNode2 = mapper.readValue("{\"lessons\":["+secondJson.getText().replaceAll("'", "\"")+"]}", ObjectNode.class);
					JsonNode lessonsNode2 = rootNode2.get("lessons");
					
					String finalJson = "";
					
					int firstID = 0;
					
	    			if(lessonsNode1 != null && lessonsNode1.isArray()) {
	    				Iterator<JsonNode> it = lessonsNode1.elements();
	    				
	    				while(it.hasNext()) {
	    					if(!finalJson.isEmpty())
	    						finalJson += ",";
	    						
	    					ObjectNode lesson = (ObjectNode)it.next();
	    					
	    					lesson.put("id", ""+firstID++);

	    					finalJson += mapper.writeValueAsString(lesson).replace('"', '\'');
	    				}
	    			}
	    			
	    			if(lessonsNode2 != null && lessonsNode2.isArray()) {
	    				Iterator<JsonNode> it = lessonsNode2.elements();
	    				
	    				while(it.hasNext()) {
	    					if(!finalJson.isEmpty())
	    						finalJson += ",";
	    						
	    					ObjectNode lesson = (ObjectNode)it.next();
	    					
	    					lesson.put("id", ""+firstID++);

	    					finalJson += mapper.writeValueAsString(lesson).replace('"', '\'');
	    				}
	    			}
	    			
	    			finalLessonSequence.setText(""+(firstID));
	    			finalJsonText.setText(finalJson);
	    			
				} catch (JsonParseException e) {
					Alert.alert(MessageType.ERROR, "JsonParseException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				} catch (JsonMappingException e) {
					Alert.alert(MessageType.ERROR, "JsonMappingException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				} catch (IOException e) {
					Alert.alert(MessageType.ERROR, "IOException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				}
            }
        });
        
        checkGroupButton1.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
            	try {
	            	ObjectNode rootNode1 = mapper.readValue("{\"lessons\":["+firstJson.getText().replaceAll("'", "\"")+"]}", ObjectNode.class);
					JsonNode lessonsNode1 = rootNode1.get("lessons");
					
					HashMap<String, Integer> groupMap = new HashMap<String, Integer>();
					
					if(lessonsNode1 != null && lessonsNode1.isArray()) {
	    				Iterator<JsonNode> it = lessonsNode1.elements();
	    				
	    				while(it.hasNext()) {
	    					JsonNode lesson = it.next();
	    					
	    					if(lesson.get("id")!=null && !lesson.get("status").textValue().equalsIgnoreCase("deleted") && lesson.get("groupName")!=null && !lesson.get("groupName").textValue().isEmpty()) {
	    						if(groupMap.containsKey(lesson.get("groupName").textValue()))
	    							groupMap.put(lesson.get("groupName").textValue(), groupMap.get(lesson.get("groupName").textValue())+1);
	    						else
	    							groupMap.put(lesson.get("groupName").textValue(), 1);
	    					}
	    				}
	    			}
					
					String resultString = "";
					
					int count = 0;
					for(String group : groupMap.keySet()) {
						count += groupMap.get(group);
					}
					
					for(String group : groupMap.keySet()) {
						if(!resultString.isEmpty())
							resultString += ", ";
						
						resultString += group+"("+groupMap.get(group)+")";
					}
					resultString = "Lessons("+count+"); " + resultString;
					
					checkGroupText1.setText(resultString);
            	} catch (JsonParseException e) {
					Alert.alert(MessageType.ERROR, "JsonParseException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				} catch (JsonMappingException e) {
					Alert.alert(MessageType.ERROR, "JsonMappingException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				} catch (IOException e) {
					Alert.alert(MessageType.ERROR, "IOException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				}
            }
        });
        
        checkGroupButton2.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
            	try {
	            	ObjectNode rootNode1 = mapper.readValue("{\"lessons\":["+secondJson.getText().replaceAll("'", "\"")+"]}", ObjectNode.class);
					JsonNode lessonsNode1 = rootNode1.get("lessons");
					
					HashMap<String, Integer> groupMap = new HashMap<String, Integer>();
					
					if(lessonsNode1 != null && lessonsNode1.isArray()) {
	    				Iterator<JsonNode> it = lessonsNode1.elements();
	    				
	    				while(it.hasNext()) {
	    					JsonNode lesson = it.next();
	    					
	    					if(lesson.get("id")!=null && !lesson.get("status").textValue().equalsIgnoreCase("deleted") && lesson.get("groupName")!=null && !lesson.get("groupName").textValue().isEmpty()) {
	    						if(groupMap.containsKey(lesson.get("groupName").textValue()))
	    							groupMap.put(lesson.get("groupName").textValue(), groupMap.get(lesson.get("groupName").textValue())+1);
	    						else
	    							groupMap.put(lesson.get("groupName").textValue(), 1);
	    					}
	    				}
	    			}
					
					String resultString = "";
					
					int count = 0;
					for(String group : groupMap.keySet()) {
						count += groupMap.get(group);
					}
					
					
					for(String group : groupMap.keySet()) {
						if(!resultString.isEmpty())
							resultString += ", ";
						
						resultString += group+"("+groupMap.get(group)+")";
					}
					resultString = "Lessons("+count+"); " + resultString;
					
					checkGroupText2.setText(resultString);
            	} catch (JsonParseException e) {
					Alert.alert(MessageType.ERROR, "JsonParseException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				} catch (JsonMappingException e) {
					Alert.alert(MessageType.ERROR, "JsonMappingException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				} catch (IOException e) {
					Alert.alert(MessageType.ERROR, "IOException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				}
            }
        });
        
        checkGroupButton3.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
            	try {
	            	ObjectNode rootNode1 = mapper.readValue("{\"lessons\":["+finalJsonText.getText().replaceAll("'", "\"")+"]}", ObjectNode.class);
					JsonNode lessonsNode1 = rootNode1.get("lessons");
					
					HashMap<String, Integer> groupMap = new HashMap<String, Integer>();
					
					if(lessonsNode1 != null && lessonsNode1.isArray()) {
	    				Iterator<JsonNode> it = lessonsNode1.elements();
	    				
	    				while(it.hasNext()) {
	    					JsonNode lesson = it.next();
	    					
	    					if(lesson.get("id")!=null && !lesson.get("status").textValue().equalsIgnoreCase("deleted") && lesson.get("groupName")!=null && !lesson.get("groupName").textValue().isEmpty()) {
	    						if(groupMap.containsKey(lesson.get("groupName").textValue()))
	    							groupMap.put(lesson.get("groupName").textValue(), groupMap.get(lesson.get("groupName").textValue())+1);
	    						else
	    							groupMap.put(lesson.get("groupName").textValue(), 1);
	    					}
	    				}
	    			}
					
					String resultString = "";
					
					int count = 0;
					for(String group : groupMap.keySet()) {
						count += groupMap.get(group);
					}
					
					
					for(String group : groupMap.keySet()) {
						if(!resultString.isEmpty())
							resultString += ", ";
						
						resultString += group+"("+groupMap.get(group)+")";
					}
					resultString = "Lessons("+count+"); " + resultString;
					
					checkGroupText3.setText(resultString);
            	} catch (JsonParseException e) {
					Alert.alert(MessageType.ERROR, "JsonParseException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				} catch (JsonMappingException e) {
					Alert.alert(MessageType.ERROR, "JsonMappingException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				} catch (IOException e) {
					Alert.alert(MessageType.ERROR, "IOException. Ask from Nurlan Rakhimzhanov.", KaspandrWindow.this);
					e.printStackTrace();
				}
            }
        });
    }
}
