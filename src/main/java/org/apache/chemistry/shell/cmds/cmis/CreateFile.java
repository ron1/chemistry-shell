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
import org.apache.chemistry.shell.app.Context;
import org.apache.chemistry.shell.command.Cmd;
import org.apache.chemistry.shell.command.CommandException;
import org.apache.chemistry.shell.command.CommandLine;
import org.apache.chemistry.shell.util.DummyFileType;
import org.apache.chemistry.shell.util.Path;
import org.apache.chemistry.shell.util.SimpleCreator;

@Cmd(syntax="mkfile|mkdoc [-t|--type:*] [--zero] [--one] [--text] [--random] target:item [-s|--size:*]", synopsis="Create a document of the given name")
public class CreateFile extends ChemistryCommand {

    @Override
    protected void execute(ChemistryApp app, CommandLine cmdLine)
            throws Exception {
        String param = cmdLine.getParameterValue("target");
        String typeName = cmdLine.getParameterValue("-t");
        if (typeName == null) {
            typeName = "cmis:document";
        }
        DummyFileType dummyType = DummyFileType.ZEROS;
        if(cmdLine.getParameter("--zero") != null)
        	dummyType = DummyFileType.ZEROS;
        else if(cmdLine.getParameter("--one") != null)
        	dummyType = DummyFileType.ONES;
        else if(cmdLine.getParameter("--text") != null)
        	dummyType = DummyFileType.TEXT;
        else if(cmdLine.getParameter("--random") != null)
        	dummyType = DummyFileType.RANDOM;
        long size = -1;
        if(cmdLine.getParameterValue("-s")!=null)
           size = Long.parseLong(cmdLine.getParameterValue("-s"));
        Path path = new Path(param);
        String name = path.getLastSegment();
        Path parent = path.getParent();

        Context ctx = app.resolveContext(parent);
        Folder folder = ctx.as(Folder.class);
        if (folder == null) {
            throw new CommandException(parent+" doesn't exist or is not a folder");
        }
        if(size < 0)
        	new SimpleCreator(folder).createFile(typeName, name);
        else
        	new SimpleCreator(folder).createDummyFile(typeName, name, dummyType, size);
        ctx.reset();
    }

}
