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

package org.apache.chemistry.shell.util;

import java.io.IOException;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.shell.app.Console;

public class SimpleBrowser {

    protected final Folder root;

    public SimpleBrowser(Folder root) {
        this.root = root;
    }

    public void browse() throws IOException {
        doBrowse(root);
    }

    protected void doBrowse(Folder currentNode) throws IOException {
        doBrowse("+", currentNode);
    }

    protected void doBrowse(String tabs, Folder currentNode) throws IOException {
        dumpWithPath(tabs, currentNode);
        ItemIterable<CmisObject> children = currentNode.getChildren();
        for (CmisObject child : children) {
            if (ObjectType.FOLDER_BASETYPE_ID.equals(child.getType())) {
                Folder folder = (Folder) child;
                doBrowse(tabs + "--+", folder);
            } else {
                dumpWithPath(tabs + "---", child);
            }
        }
    }

    protected void dumpWithPath(String tabs, CmisObject item) {
        Console.getDefault().println(tabs+ " "+ item.getName()+" ["+item.getType().getId()+"]");
    }

}
