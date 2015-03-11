package org.bark.remoting;

public class Handler {
	private Object[] refObjects = {};
	
	public int sharedHandler(Object obj){
		int length = refObjects.length;
		for(int i=0; i<length; i++){
			if(obj == refObjects[i]){
				return i;
			}
		}
		
		System.arraycopy(refObjects, 0, refObjects = new Object[length + 1], 0, length);
		refObjects[length] = obj;
		return length;
	}
	
	public Object[] getShared(){
		return refObjects;
	}

	public Object getObject(int refId) {
		return refObjects[refId];
	}
}
