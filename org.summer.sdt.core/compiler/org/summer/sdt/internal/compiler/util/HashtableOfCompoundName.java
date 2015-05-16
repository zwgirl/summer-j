/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.summer.sdt.internal.compiler.util;

import org.summer.sdt.core.compiler.CharOperation;

 /**
  *	Hashtable for non-zero int keys.
  */
//cym 2015-04-23
public final class HashtableOfCompoundName {
	// to avoid using Enumerations, walk the individual tables skipping nulls
	public char[] keyTable[];
	public char[][][] valueTable;

	public int elementSize; // number of elements in the table
	int threshold;
	public HashtableOfCompoundName() {
		this(3); // usually not very large
	}
	public HashtableOfCompoundName(int size) {
		this.elementSize = 0;
		this.threshold = size; // size represents the expected number of elements
		int extraRoom = (int) (size * 1.75f);
		if (this.threshold == extraRoom)
			extraRoom++;
		this.keyTable = new char[extraRoom][];
		this.valueTable = new char[extraRoom][][];
	}
	public boolean containsKey(char[] key) {
		int length = this.keyTable.length,
			index = CharOperation.hashCode(key) % length;
		int keyLength = key.length;
		char[] currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (currentKey.length == keyLength && CharOperation.equals(currentKey, key))
				return true;
			if (++index == length) {
				index = 0;
			}
		}
		return false;
	}
	public char[][] get(char[] key) {
		int length = this.keyTable.length,
			index = CharOperation.hashCode(key) % length;
		int keyLength = key.length;
		char[] currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (currentKey.length == keyLength && CharOperation.equals(currentKey, key))
				return this.valueTable[index];
			if (++index == length) {
				index = 0;
			}
		}
		return null;
	}
	public char[][] put(char[] key, char[][] value) {
		int length = this.keyTable.length,
			index = CharOperation.hashCode(key) % length;
		int keyLength = key.length;
		char[] currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (currentKey.length == keyLength && CharOperation.equals(currentKey, key))
				return this.valueTable[index] = value;
			if (++index == length) {
				index = 0;
			}
		}
		this.keyTable[index] = key;
		this.valueTable[index] = value;
	
		// assumes the threshold is never equal to the size of the table
		if (++this.elementSize > this.threshold)
			rehash();
		return value;
	}
	private void rehash() {
		HashtableOfCompoundName newHashtable = new HashtableOfCompoundName(this.elementSize * 2); // double the number of expected elements
		char[] currentKey;
		for (int i = this.keyTable.length; --i >= 0;)
			if ((currentKey = this.keyTable[i]) != null)
				newHashtable.put(currentKey, this.valueTable[i]);
	
		this.keyTable = newHashtable.keyTable;
		this.valueTable = newHashtable.valueTable;
		this.threshold = newHashtable.threshold;
	}
	public int size() {
		return this.elementSize;
	}
	public String toString() {
		String s = ""; //$NON-NLS-1$
		char[][] pkg;
		for (int i = 0, length = this.valueTable.length; i < length; i++)
			if ((pkg = this.valueTable[i]) != null)
				s += pkg.toString() + "\n"; //$NON-NLS-1$
		return s;
	}
}
