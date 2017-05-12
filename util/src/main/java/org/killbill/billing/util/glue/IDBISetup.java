/*
 * Copyright 2014-2017 Groupon, Inc
 * Copyright 2014-2017 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
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

package org.killbill.billing.util.glue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.killbill.billing.lifecycle.ServiceFinder;
import org.killbill.billing.util.dao.AuditLogModelDaoMapper;
import org.killbill.billing.util.dao.EntityHistoryModelDaoMapperFactory;
import org.killbill.billing.util.dao.RecordIdIdMappingsMapper;
import org.killbill.billing.util.entity.Entity;
import org.killbill.billing.util.entity.dao.EntitySqlDao;
import org.killbill.billing.util.security.shiro.dao.SessionModelDao;
import org.killbill.billing.util.validation.dao.DatabaseSchemaSqlDao;
import org.killbill.commons.jdbi.mapper.LowerToCamelBeanMapperFactory;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class IDBISetup {

    public static List<? extends ResultSetMapperFactory> mapperFactoriesToRegister() {
        final Builder<ResultSetMapperFactory> builder = ImmutableList.<ResultSetMapperFactory>builder();
        builder.add(new LowerToCamelBeanMapperFactory(SessionModelDao.class));

        final ServiceFinder<EntitySqlDao> serviceFinder = new ServiceFinder<EntitySqlDao>(IDBISetup.class.getClassLoader(), EntitySqlDao.class.getName());
        for (final Class<? extends EntitySqlDao> sqlObjectType : serviceFinder.getServices()) {
            // Find the model class associated with this sqlObjectType (which is a SqlDao class) to register its mapper
            // If a custom mapper is defined via @RegisterMapper, don't register our generic one
            if (sqlObjectType.getGenericInterfaces() != null &&
                sqlObjectType.getAnnotation(RegisterMapper.class) == null) {
                for (int i = 0; i < sqlObjectType.getGenericInterfaces().length; i++) {
                    if (sqlObjectType.getGenericInterfaces()[i] instanceof ParameterizedType) {
                        final ParameterizedType type = (ParameterizedType) sqlObjectType.getGenericInterfaces()[i];
                        for (int j = 0; j < type.getActualTypeArguments().length; j++) {
                            final Type modelType = type.getActualTypeArguments()[j];
                            if (modelType instanceof Class) {
                                final Class modelClazz = (Class) modelType;
                                if (Entity.class.isAssignableFrom(modelClazz)) {
                                    builder.add(new LowerToCamelBeanMapperFactory(modelClazz));
                                    builder.add(new EntityHistoryModelDaoMapperFactory(modelClazz, sqlObjectType));
                                }
                            }
                        }
                    }
                }
            }
        }

        return builder.build();
    }

    public static List<? extends ResultSetMapper> mappersToRegister() {
        return ImmutableList.<ResultSetMapper>builder()
                .add(new AuditLogModelDaoMapper())
                .add(new RecordIdIdMappingsMapper())
                .add(new DatabaseSchemaSqlDao.ColumnInfoMapper())
                .build();
    }
}
