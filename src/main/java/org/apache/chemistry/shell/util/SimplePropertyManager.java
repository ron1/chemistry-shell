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
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.chemistry.shell.app.Console;
import org.apache.chemistry.shell.command.CommandException;

public class SimplePropertyManager {

	protected final CmisObject item;

	public SimplePropertyManager(CmisObject item) {
		this.item = item;
	}

	public String getPropertyAsString(String name) {
		Property<Object> p = item.getProperty(name);
		if (p == null) {
			return "[null]";
		}
		Object val = p.getValue();
		if (val instanceof Calendar) {
			Calendar cal = (Calendar) val;
			SimpleDateFormat df = new SimpleDateFormat();
			return df.format(cal.getTime());

		} else {
			return val != null ? val.toString() : "[null]";
		}
	}

	public void setProperty(String name, String value) throws Exception {
//		Property<Object> p = item.getProperty(name);
//		if (p == null) {
//			return;
//		}
		Map<String, Object> properties = new HashMap<String, Object>();
//		if (p.getType() == PropertyType.DATETIME) {
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(new SimpleDateFormat().parse(value));
//			properties.put(name, cal);
//		}else{
			properties.put(name, value);
//		}
		item.updateProperties(properties);
	}

	public void dumpProperties() {
		List<Property<?>> props = item.getProperties();

		for (Property<?> prop : props) {
			Object value = prop.getValue();
			String valueAsString;
			if (value instanceof Calendar) {
				Calendar cal = (Calendar) value;
				SimpleDateFormat df = new SimpleDateFormat();
				valueAsString = df.format(cal.getTime());
			} else if (value != null) {
				valueAsString = value.toString();
			} else {
				valueAsString = "[null]";
			}
			Console.getDefault().println(prop.getId()+ " ["
			+ prop.getDefinition().getUpdatability().toString() +"] = " + valueAsString);
		}
	}

	public ContentStream getStream() throws IOException, CommandException {
		if (item instanceof Document) {
			Document doc = (Document) item;
			return doc.getContentStream();
		} else {
			throw new CommandException(
					"Target object is not a Document, can not get stream");
		}
	}

	public void setStream(InputStream in, long filesize, String name)
			throws Exception {
		if (item instanceof Document) {
			Document doc = (Document) item;
			String mimeType = MimeTypeHelper.getMimeType(name);
			ContentStream stream = new ContentStreamImpl(name,
					BigInteger.valueOf(filesize), mimeType, in);
			doc.setContentStream(stream, true);
		} else {
			throw new CommandException(
					"Target object is not a Document, can not set stream");
		}
	}

}
