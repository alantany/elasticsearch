/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.search.aggregations.bucket.geogrid;

import org.apache.lucene.index.IndexWriter;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.search.aggregations.InternalAggregations;
import org.elasticsearch.search.aggregations.InternalMultiBucketAggregationTestCase;
import org.elasticsearch.search.aggregations.ParsedMultiBucketAggregation;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternalGeoHashGridTests extends InternalMultiBucketAggregationTestCase<InternalGeoHashGrid> {

    @Override
    protected InternalGeoHashGrid createTestInstance(String name,
                                                     List<PipelineAggregator> pipelineAggregators,
                                                     Map<String, Object> metaData,
                                                     InternalAggregations aggregations) {
        int size = randomIntBetween(1, 3);
        List<InternalGeoHashGrid.Bucket> buckets = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            double latitude = randomDoubleBetween(-90.0, 90.0, false);
            double longitude = randomDoubleBetween(-180.0, 180.0, false);

            long geoHashAsLong = GeoHashUtils.longEncode(longitude, latitude, 4);
            buckets.add(new InternalGeoHashGrid.Bucket(geoHashAsLong, randomInt(IndexWriter.MAX_DOCS), aggregations));
        }
        return new InternalGeoHashGrid(name, size, buckets, pipelineAggregators, metaData);
    }

    @Override
    protected Class<? extends ParsedMultiBucketAggregation> implementationClass() {
        return ParsedGeoHashGrid.class;
    }
}
