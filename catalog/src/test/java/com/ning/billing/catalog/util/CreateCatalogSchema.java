/*
 * Copyright 2010-2013 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.billing.catalog.util;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import com.ning.billing.catalog.StandaloneCatalog;
import com.ning.billing.util.config.catalog.XMLSchemaGenerator;

// Tool to print the catalog XML Schema (XSD)
public class CreateCatalogSchema {

    public static void main(final String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: <filepath>");
            System.exit(0);
        }

        final File f = new File(args[0]);
        final Writer w = new FileWriter(f);
        w.write(XMLSchemaGenerator.xmlSchemaAsString(StandaloneCatalog.class));
        w.close();
    }
}
