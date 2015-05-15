package org.bark.remoting;

public class ReferenceProcessor {
	private Object[] references = {};

	public int shared(Object obj) {
		int length = references.length;
		for (int i = 0; i < length; i++) {
			if (obj == references[i]) {
				return i;
			}
		}

		System.arraycopy(references, 0, references = new Object[length + 1], 0, length);
		references[length] = obj;
		return length;
	}

	public Object[] getShares() {
		return references;
	}

	public Object getShared(int refId) {
		return references[refId];
	}
}
