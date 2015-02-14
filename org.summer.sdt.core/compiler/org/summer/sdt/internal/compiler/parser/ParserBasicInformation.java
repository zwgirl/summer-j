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
    NUM_STATES        = 1068,

    NT_OFFSET         = 128,
    SCOPE_UBOUND      = 295,
    SCOPE_SIZE        = 296,
    LA_STATE_OFFSET   = 16114,
    MAX_LA            = 1,
    NUM_RULES         = 849,
    NUM_TERMINALS     = 128,
    NUM_NON_TERMINALS = 386,
    NUM_SYMBOLS       = 514,
    START_STATE       = 908,
    EOFT_SYMBOL       = 62,
    EOLT_SYMBOL       = 62,
    ACCEPT_ACTION     = 16113,
    ERROR_ACTION      = 16114;
}
