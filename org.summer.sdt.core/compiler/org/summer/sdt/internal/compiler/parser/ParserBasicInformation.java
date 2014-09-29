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

	int 
    ERROR_SYMBOL      = 128,
    MAX_NAME_LENGTH   = 41,
    NUM_STATES        = 1163,

    NT_OFFSET         = 128,
    SCOPE_UBOUND      = 165,
    SCOPE_SIZE        = 166,
    LA_STATE_OFFSET   = 14873,
    MAX_LA            = 1,
    NUM_RULES         = 847,
    NUM_TERMINALS     = 128,
    NUM_NON_TERMINALS = 387,
    NUM_SYMBOLS       = 515,
    START_STATE       = 1585,
    EOFT_SYMBOL       = 74,
    EOLT_SYMBOL       = 74,
    ACCEPT_ACTION     = 14872,
    ERROR_ACTION      = 14873;
}
