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

package org.apache.chemistry.shell.app;

import org.apache.chemistry.shell.command.Command;
import org.apache.chemistry.shell.command.CommandLine;

public abstract class ChemistryCommand extends Command {

    @Override
    public void run(Application app, CommandLine cmdLine) throws Exception {
        if (app instanceof ChemistryApp) {
            ensureConnected(app);
            execute((ChemistryApp) app, cmdLine);
        } else {
            Console.getDefault().error(
                    "Chemistry commands cannot be run outside chemistry context");
        }
    }

    protected abstract void execute(ChemistryApp app, CommandLine cmdLine)
            throws Exception;

}
