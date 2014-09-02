/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.summer.sdt.internal.compiler.parser;

/*An interface that contains static declarations for some basic information
 about the parser such as the number of rules in the grammar, the starting state, etc...*/
public interface ParserBasicInformation {

//	int ERROR_SYMBOL = 118,
//		MAX_NAME_LENGTH = 41,
//		NUM_STATES = 1092,
//
//		NT_OFFSET = 118,
//		SCOPE_UBOUND = 282,
//		SCOPE_SIZE = 283,
//		LA_STATE_OFFSET = 15802,
//		MAX_LA = 1,
//		NUM_RULES = 796,
//		NUM_TERMINALS = 118,
//		NUM_NON_TERMINALS = 359,
//		NUM_SYMBOLS = 477,
//		START_STATE = 886,
//		EOFT_SYMBOL = 60,
//		EOLT_SYMBOL = 60,
//		ACCEPT_ACTION = 15801,
//		ERROR_ACTION = 15802;
	
    int 
    ERROR_SYMBOL      = 128,
    MAX_NAME_LENGTH   = 41,
    NUM_STATES        = 1101,

    NT_OFFSET         = 128,
    SCOPE_UBOUND      = 158,
    SCOPE_SIZE        = 159,
    LA_STATE_OFFSET   = 13966,
    MAX_LA            = 1,
    NUM_RULES         = 822,
    NUM_TERMINALS     = 128,
    NUM_NON_TERMINALS = 377,
    NUM_SYMBOLS       = 505,
    START_STATE       = 936,
    EOFT_SYMBOL       = 73,
    EOLT_SYMBOL       = 73,
    ACCEPT_ACTION     = 13965,
    ERROR_ACTION      = 13966;
}
