package kz.nurlan.kaspandr;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.ActivityIndicator;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.BoxPane;
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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class KaspandrWindow extends Window implements Bindable {
    
	@BXML private ActivityIndicator activityIndicator = null;
	@BXML private BoxPane activityIndicatorBoxPane = null;
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
    
    private HashMap<String, ArrayNode> groupByLessonsByGroup(String lessons) {
    	HashMap<String, ArrayNode> groupMap = new HashMap<String, ArrayNode>();
    	try {
        	ObjectNode rootNode1 = mapper.readValue("{\"lessons\":["+lessons+"]}", ObjectNode.class);
        	JsonNode lessonsNode1 = rootNode1.get("lessons");
        	
			if(lessonsNode1 != null && lessonsNode1.isArray()) {
				Iterator<JsonNode> it = lessonsNode1.elements();
				while(it.hasNext()) {
					JsonNode lesson = it.next();
					
					if(lesson.get("id")!=null && !lesson.get("status").textValue().equalsIgnoreCase("deleted") && lesson.get("groupName")!=null && !lesson.get("groupName").textValue().isEmpty()) {
						if(groupMap.containsKey(lesson.get("groupName").textValue()))
							groupMap.put(lesson.get("groupName").textValue(), groupMap.get(lesson.get("groupName").textValue()).add(lesson));
						else {
							groupMap.put(lesson.get("groupName").textValue(), mapper.createArrayNode().add(lesson));
						}
					}
				}
			}
			
    	} catch (Exception e) {
			e.printStackTrace();
		} 
    	
    	return groupMap;
    }
    
    private HashMap<String, Integer> getCountedLessonsByGroup(String lessons) {
    	HashMap<String, Integer> groupMap = new HashMap<String, Integer>();
    	try {
        	ObjectNode rootNode1 = mapper.readValue("{\"lessons\":["+lessons+"]}", ObjectNode.class);
			JsonNode lessonsNode1 = rootNode1.get("lessons");
			
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
			
    	} catch (Exception e) {
			e.printStackTrace();
		} 
    	
    	return groupMap;
    }
    
    private String checkLessonCountByGroup(String lessons) throws JsonParseException, JsonMappingException, IOException {
    	String resultString = "";
    	
		ObjectNode rootNode1 = mapper.readValue("{\"lessons\":["+lessons+"]}", ObjectNode.class);
		JsonNode lessonsNode1 = rootNode1.get("lessons");
		
		TreeMap<String, Integer> groupMap = new TreeMap<String, Integer>();
		
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
    	
    	return resultString;
    }
    
    @Override
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
    	mapper = new ObjectMapper();
        jsonMergeButton.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
            	activityIndicatorBoxPane.setVisible(true);
				try {
					String finalJson = "";
					
					int firstID = 0;
					
					HashSet<String> groupNameSet = new HashSet<String>();
					
					HashMap<String, ArrayNode> grouppedLessonsByGroupMap1 = groupByLessonsByGroup(firstJson.getText().replaceAll("'", "\""));
					HashMap<String, ArrayNode> grouppedLessonsByGroupMap2 = groupByLessonsByGroup(secondJson.getText().replaceAll("'", "\""));
					HashMap<String, Integer> lessonCountByGroupMap1 = getCountedLessonsByGroup(firstJson.getText().replaceAll("'", "\""));
					HashMap<String, Integer> lessonCountByGroupMap2 = getCountedLessonsByGroup(secondJson.getText().replaceAll("'", "\""));
					
					
					for(String groupName : grouppedLessonsByGroupMap1.keySet()) {
						groupNameSet.add(groupName);
					}
					for(String groupName : grouppedLessonsByGroupMap2.keySet()) {
						groupNameSet.add(groupName);
					}
					
    				Iterator<String> it = groupNameSet.iterator();
    				
    				HashMap<String, ArrayNode> linkForGrouppedLessonsByGroupMap = grouppedLessonsByGroupMap1;
    				while(it.hasNext()) {
    					String groupName = it.next();
    					
    					if(lessonCountByGroupMap1.get(groupName) != null && lessonCountByGroupMap2.get(groupName) == null) 
    						linkForGrouppedLessonsByGroupMap = grouppedLessonsByGroupMap1;
    					else if(lessonCountByGroupMap1.get(groupName) == null && lessonCountByGroupMap2.get(groupName) != null)
    						linkForGrouppedLessonsByGroupMap = grouppedLessonsByGroupMap2;
    					else if(lessonCountByGroupMap1.get(groupName) > lessonCountByGroupMap2.get(groupName)) 
    						linkForGrouppedLessonsByGroupMap = grouppedLessonsByGroupMap1;
    					else
    						linkForGrouppedLessonsByGroupMap = grouppedLessonsByGroupMap2;
    					
    					ArrayNode lessonArrayNode = linkForGrouppedLessonsByGroupMap.get(groupName);
    					
    					Iterator<JsonNode> lanIt = lessonArrayNode.elements();
	    				
	    				while(lanIt.hasNext()) {
	    					if(!finalJson.isEmpty())
	    						finalJson += ",";
	    						
	    					ObjectNode lesson = (ObjectNode)lanIt.next();
	    					
	    					lesson.put("id", ""+firstID++);

	    					finalJson += mapper.writeValueAsString(lesson).replace('"', '\'');
	    				}
    				}
	    			
	    			finalLessonSequence.setText(""+(firstID));
	    			finalJsonText.setText(finalJson);
				} catch (JsonParseException e) {
					Alert.alert(MessageType.ERROR, "JsonParseException. Make bug report to Nurlan Rakhimzhanov(nurlan.rakhimzhanov@bee.kz).", KaspandrWindow.this);
					e.printStackTrace();
				} catch (JsonMappingException e) {
					Alert.alert(MessageType.ERROR, "JsonMappingException. Make bug report to Nurlan Rakhimzhanov(nurlan.rakhimzhanov@bee.kz).", KaspandrWindow.this);
					e.printStackTrace();
				} catch (IOException e) {
					Alert.alert(MessageType.ERROR, "IOException. Make bug report to Nurlan Rakhimzhanov(nurlan.rakhimzhanov@bee.kz).", KaspandrWindow.this);
					e.printStackTrace();
				}
				activityIndicatorBoxPane.setVisible(false);
            }
        });
        
        checkGroupButton1.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
            	try {
					checkGroupText1.setText(checkLessonCountByGroup(firstJson.getText().replaceAll("'", "\"")));
            	} catch (JsonParseException e) {
            		Alert.alert(MessageType.ERROR, "Make bug report to Nurlan Rakhimzhanov\n(nurlan.rakhimzhanov@bee.kz).","JsonParseException",null,KaspandrWindow.this,null);
					e.printStackTrace();
				} catch (JsonMappingException e) {
					Alert.alert(MessageType.ERROR, "Make bug report to Nurlan Rakhimzhanov\n(nurlan.rakhimzhanov@bee.kz).","JsonMappingException",null,KaspandrWindow.this,null);
					e.printStackTrace();
				} catch (IOException e) {
					Alert.alert(MessageType.ERROR, "Make bug report to Nurlan Rakhimzhanov\n(nurlan.rakhimzhanov@bee.kz).","IOException",null,KaspandrWindow.this,null);
					e.printStackTrace();
				}
            }
        });
        
        checkGroupButton2.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
            	try {
            		checkGroupText2.setText(checkLessonCountByGroup(secondJson.getText().replaceAll("'", "\"")));
            	} catch (JsonParseException e) {
            		Alert.alert(MessageType.ERROR, "Make bug report to Nurlan Rakhimzhanov\n(nurlan.rakhimzhanov@bee.kz).","JsonParseException",null,KaspandrWindow.this,null);
					e.printStackTrace();
				} catch (JsonMappingException e) {
					Alert.alert(MessageType.ERROR, "Make bug report to Nurlan Rakhimzhanov\n(nurlan.rakhimzhanov@bee.kz).","JsonMappingException",null,KaspandrWindow.this,null);
					e.printStackTrace();
				} catch (IOException e) {
					Alert.alert(MessageType.ERROR, "Make bug report to Nurlan Rakhimzhanov\n(nurlan.rakhimzhanov@bee.kz).","IOException",null,KaspandrWindow.this,null);
					e.printStackTrace();
				}
            }
        });
        
        checkGroupButton3.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
            	try {
            		checkGroupText3.setText(checkLessonCountByGroup(finalJsonText.getText().replaceAll("'", "\"")));
            	} catch (JsonParseException e) {
            		Alert.alert(MessageType.ERROR, "Make bug report to Nurlan Rakhimzhanov\n(nurlan.rakhimzhanov@bee.kz).","JsonParseException",null,KaspandrWindow.this,null);
					e.printStackTrace();
				} catch (JsonMappingException e) {
					Alert.alert(MessageType.ERROR, "Make bug report to Nurlan Rakhimzhanov\n(nurlan.rakhimzhanov@bee.kz).","JsonMappingException",null,KaspandrWindow.this,null);
					e.printStackTrace();
				} catch (IOException e) {
					Alert.alert(MessageType.ERROR, "Make bug report to Nurlan Rakhimzhanov\n(nurlan.rakhimzhanov@bee.kz).","IOException",null,KaspandrWindow.this,null);
					e.printStackTrace();
				}
            }
        });
    }
}
