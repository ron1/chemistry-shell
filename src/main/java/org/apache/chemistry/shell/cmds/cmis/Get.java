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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.commons.io.IOUtils;
import org.apache.chemistry.shell.app.ChemistryApp;
import org.apache.chemistry.shell.app.ChemistryCommand;
import org.apache.chemistry.shell.app.Context;
import org.apache.chemistry.shell.command.Cmd;
import org.apache.chemistry.shell.command.CommandException;
import org.apache.chemistry.shell.command.CommandLine;
import org.apache.chemistry.shell.util.Path;
import org.apache.chemistry.shell.util.SimplePropertyManager;

// TODO: add an optional local name for the file
@Cmd(syntax="get|getstream target:item", synopsis="Downloads the stream of the target document")
public class Get extends ChemistryCommand {

    @Override
    protected void execute(ChemistryApp app, CommandLine cmdLine)
            throws Exception {

        String target = cmdLine.getParameterValue("target");

        Context ctx = app.resolveContext(new Path(target));
        if (ctx == null) {
            throw new CommandException("Cannot resolve "+target);
        }

        Document obj = ctx.as(Document.class);
        if (obj == null) {
            throw new CommandException("Your target must be a document");
        }

        ContentStream cs = new SimplePropertyManager(obj).getStream();
        if (cs == null) {
            throw new CommandException("Your target doesn't have a stream");
        }

        String name = cs.getFileName();
        InputStream in = cs.getStream();
        File file = app.resolveFile(name);
        FileOutputStream out = new FileOutputStream(file);
        try {
            IOUtils.copy(in, out);
            println("Object stream saved to local file: " + file);
        } finally {
            out.close();
            in.close();
        }
    }

}
