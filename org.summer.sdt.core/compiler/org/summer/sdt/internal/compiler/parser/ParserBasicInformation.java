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
    ERROR_SYMBOL      = 126,
    MAX_NAME_LENGTH   = 41,
    NUM_STATES        = 1096,

    NT_OFFSET         = 126,
    SCOPE_UBOUND      = 299,
    SCOPE_SIZE        = 300,
    LA_STATE_OFFSET   = 16076,
    MAX_LA            = 1,
    NUM_RULES         = 867,
    NUM_TERMINALS     = 126,
    NUM_NON_TERMINALS = 397,
    NUM_SYMBOLS       = 523,
    START_STATE       = 992,
    EOFT_SYMBOL       = 60,
    EOLT_SYMBOL       = 60,
    ACCEPT_ACTION     = 16075,
    ERROR_ACTION      = 16076;
}
