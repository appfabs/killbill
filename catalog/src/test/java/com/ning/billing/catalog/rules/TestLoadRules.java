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

package com.ning.billing.catalog.rules;

import java.net.URI;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ning.billing.catalog.CatalogTestSuiteNoDB;
import com.ning.billing.catalog.StandaloneCatalog;
import com.ning.billing.catalog.api.BillingPeriod;
import com.ning.billing.catalog.api.PlanAlignmentCreate;
import com.ning.billing.catalog.api.PlanSpecifier;
import com.ning.billing.catalog.api.ProductCategory;
import com.ning.billing.util.config.catalog.XMLLoader;

import com.google.common.io.Resources;

public class TestLoadRules extends CatalogTestSuiteNoDB {

    @Test(groups = "fast")
    public void test() throws Exception {
        final URI uri = new URI(Resources.getResource("WeaponsHireSmall.xml").toExternalForm());
        final StandaloneCatalog catalog = XMLLoader.getObjectFromUri(uri, StandaloneCatalog.class);
        Assert.assertNotNull(catalog);
        final PlanRules rules = catalog.getPlanRules();

        final PlanSpecifier specifier = new PlanSpecifier("Laser-Scope", ProductCategory.ADD_ON, BillingPeriod.MONTHLY,
                                                          "DEFAULT");

        final PlanAlignmentCreate alignment = rules.getPlanCreateAlignment(specifier, catalog);
        Assert.assertEquals(alignment, PlanAlignmentCreate.START_OF_SUBSCRIPTION);

        final PlanSpecifier specifier2 = new PlanSpecifier("Extra-Ammo", ProductCategory.ADD_ON, BillingPeriod.MONTHLY,
                                                           "DEFAULT");

        final PlanAlignmentCreate alignment2 = rules.getPlanCreateAlignment(specifier2, catalog);
        Assert.assertEquals(alignment2, PlanAlignmentCreate.START_OF_BUNDLE);
    }
}
