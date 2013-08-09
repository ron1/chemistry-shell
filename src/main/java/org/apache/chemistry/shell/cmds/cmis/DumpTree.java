/*
 * (C) Copyright 2009-2010 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *   Bogdan Stefanescu (bs@nuxeo.com), Nuxeo
 *   Stefane Fermigier (sf@nuxeo.com), Nuxeo
 *   Florent Guillaume (fg@nuxeo.com), Nuxeo
 */

package org.apache.chemistry.shell.cmds.cmis;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.shell.app.ChemistryApp;
import org.apache.chemistry.shell.app.ChemistryCommand;
import org.apache.chemistry.shell.app.ChemistryContext;
import org.apache.chemistry.shell.app.Context;
import org.apache.chemistry.shell.command.Cmd;
import org.apache.chemistry.shell.command.CommandException;
import org.apache.chemistry.shell.command.CommandLine;
import org.apache.chemistry.shell.util.Path;
import org.apache.chemistry.shell.util.TreeBrowser;

@Cmd(syntax = "dump|tree [target:item] [depth] ", synopsis = "Dump a subtree")
public class DumpTree extends ChemistryCommand {

	@Override
	protected void execute(ChemistryApp app, CommandLine cmdLine)
			throws Exception {
		
		String target = cmdLine.getParameterValue("target");

		Context ctx;
		if (target != null) {
			ctx = app.resolveContext(new Path(target));
			if (ctx == null) {
				throw new CommandException("Cannot resolve " + target);
			}
		} else {
			ctx = app.getContext();
		}
		
		String depthParam = cmdLine.getParameterValue("depth");
		int depth = -1;
		if (depthParam != null) {
			try {
				depth = Integer.parseInt(depthParam);
			} catch (NumberFormatException ex) {
				throw new CommandException("Given depth \"" + depthParam
						+ "\" is not a number");
			}
		}
		boolean isGetDescendantsSupported = false;
		if(ctx instanceof ChemistryContext){
			ChemistryContext context = (ChemistryContext) ctx;
			isGetDescendantsSupported = context.getRepository().getCapabilities().isGetDescendantsSupported();
		}
		Folder folder = ctx.as(Folder.class);
		if (folder != null) {
			new TreeBrowser(folder, depth, isGetDescendantsSupported).browse();
		} else {
			throw new CommandException("Target " + target + " is not a folder");
		}
	}

}
