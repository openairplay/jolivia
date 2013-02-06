/*******************************************************************************
 * Copyright (c) 2013 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - Lead developer, owner and creator
 ******************************************************************************/
/*
 * Digital Audio Access Protocol (DAAP) Library
 * Copyright (C) 2004-2010 Roger Kapsi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dyndns.jkiddo.dmap.chunks.dmap;

import org.dyndns.jkiddo.dmap.chunks.UByteChunk;
import org.dyndns.jkiddo.dmap.chunks.dmap.AuthenticationMethod.PasswordMethod;

/**
 * @author Roger Kapsi
 */
public class AuthenticationMethod extends UByteChunk
{
	public enum PasswordMethod
	{
		NO_PASSWORD(NONE), USERNAME_AND_PASSWORD(USERNAME_PASSWORD_METHOD), PASSWORD(PASSWORD_METHOD);

		private int value;

		PasswordMethod(int value)
		{
			this.value = value;
		}
	}

	private static final int NONE = 0x00;
	private static final int USERNAME_PASSWORD_METHOD = 0x01;
	private static final int PASSWORD_METHOD = 0x02;

	public AuthenticationMethod()
	{
		this(PASSWORD_METHOD);
	}

	public AuthenticationMethod(int method)
	{
		super("msau", "dmap.authenticationmethod", method);
	}

	public AuthenticationMethod(PasswordMethod password)
	{
		this(password.value);
	}
}
