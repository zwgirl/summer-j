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
    ERROR_SYMBOL      = 130,
    MAX_NAME_LENGTH   = 41,
    NUM_STATES        = 1286,

    NT_OFFSET         = 130,
    SCOPE_UBOUND      = 238,
    SCOPE_SIZE        = 239,
    LA_STATE_OFFSET   = 15538,
    MAX_LA            = 1,
    NUM_RULES         = 957,
    NUM_TERMINALS     = 130,
    NUM_NON_TERMINALS = 391,
    NUM_SYMBOLS       = 521,
    START_STATE       = 2010,
    EOFT_SYMBOL       = 75,
    EOLT_SYMBOL       = 75,
    ACCEPT_ACTION     = 15537,
    ERROR_ACTION      = 15538;
}
