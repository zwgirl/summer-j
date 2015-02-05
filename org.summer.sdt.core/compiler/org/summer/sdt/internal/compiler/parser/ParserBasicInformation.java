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
    NUM_STATES        = 1032,

    NT_OFFSET         = 128,
    SCOPE_UBOUND      = 285,
    SCOPE_SIZE        = 286,
    LA_STATE_OFFSET   = 15509,
    MAX_LA            = 1,
    NUM_RULES         = 835,
    NUM_TERMINALS     = 128,
    NUM_NON_TERMINALS = 381,
    NUM_SYMBOLS       = 509,
    START_STATE       = 1079,
    EOFT_SYMBOL       = 64,
    EOLT_SYMBOL       = 64,
    ACCEPT_ACTION     = 15508,
    ERROR_ACTION      = 15509;
}
