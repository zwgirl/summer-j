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
    ERROR_SYMBOL      = 131,
    MAX_NAME_LENGTH   = 41,
    NUM_STATES        = 1058,

    NT_OFFSET         = 131,
    SCOPE_UBOUND      = 298,
    SCOPE_SIZE        = 299,
    LA_STATE_OFFSET   = 15821,
    MAX_LA            = 1,
    NUM_RULES         = 857,
    NUM_TERMINALS     = 131,
    NUM_NON_TERMINALS = 388,
    NUM_SYMBOLS       = 519,
    START_STATE       = 1896,
    EOFT_SYMBOL       = 62,
    EOLT_SYMBOL       = 62,
    ACCEPT_ACTION     = 15820,
    ERROR_ACTION      = 15821;
}
