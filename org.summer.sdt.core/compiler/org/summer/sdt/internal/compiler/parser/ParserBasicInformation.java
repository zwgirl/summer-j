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
    ERROR_SYMBOL      = 127,
    MAX_NAME_LENGTH   = 41,
    NUM_STATES        = 1307,

    NT_OFFSET         = 127,
    SCOPE_UBOUND      = 228,
    SCOPE_SIZE        = 229,
    LA_STATE_OFFSET   = 15699,
    MAX_LA            = 1,
    NUM_RULES         = 941,
    NUM_TERMINALS     = 127,
    NUM_NON_TERMINALS = 383,
    NUM_SYMBOLS       = 510,
    START_STATE       = 2446,
    EOFT_SYMBOL       = 73,
    EOLT_SYMBOL       = 73,
    ACCEPT_ACTION     = 15698,
    ERROR_ACTION      = 15699;
}
