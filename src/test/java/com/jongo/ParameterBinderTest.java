/*
 * Copyright (C) 2011 Benoit GUEROUT <bguerout at gmail dot com> and Yves AMSELLEM <amsellem dot yves at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jongo;

import com.jongo.model.Coordinate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class ParameterBinderTest {

    private ParameterBinder binder;

    @Before
    public void setUp() throws Exception {
        binder = new ParameterBinder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithCharParameter() throws Exception {
        char c = '1';
        binder.bind("{id:#}", c);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenNotEnoughParameters() throws Exception {

        binder.bind("{id:#,id2:#}", "123");
    }

    @Test
    public void canMapParameter() throws Exception {

        String query = binder.bind("{id:#}", "123");

        assertThat(query).isEqualTo("{id:\"123\"}");
    }

    @Test
    public void canMapParameters() throws Exception {

        String query = binder.bind("{id:#, test:#}", "123", "456");

        assertThat(query).isEqualTo("{id:\"123\", test:\"456\"}");
    }

    @Test
    public void canMapDateParameter() throws Exception {

        Date epoch = new Date(0);

        String query = binder.bind("{mydate:#}", epoch);

        assertThat(query).isEqualTo("{mydate:{ \"$date\" : \"1970-01-01T00:00:00.000Z\"}}");
    }

    @Test
    public void canMapListParameter() throws Exception {

        List<String> elements = new ArrayList<String>();
        elements.add("1");
        elements.add("2");

        String query = binder.bind("{$in:#}", elements);

        assertThat(query).isEqualTo("{$in:[ \"1\" , \"2\"]}");
    }

    @Test
    public void canHandleBooleanParameter() throws Exception {


        String query = binder.bind("{id:#}", true);

        assertThat(query).isEqualTo("{id:true}");
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNonBSONPrimitive() throws Exception {

        binder.bind("{coordinate:#}", new Coordinate(0, 0));
    }
}


