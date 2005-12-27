package org.apache.maven.repository.indexing;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.analysis.TokenStream;

/**
 * Class created specifically to index artifacts
 *
 * @author Edwin Punzalan
 */
public class ArtifactRepositoryIndexAnalyzer
    extends Analyzer
{
    private Analyzer defaultAnalyzer;

    /**
     * constructor to for this analyzer
     * 
     * @character defaultAnalyzer the analyzer to use as default for the general fields of the artifact indeces
     */
    public ArtifactRepositoryIndexAnalyzer( Analyzer defaultAnalyzer )
    {
        this.defaultAnalyzer = defaultAnalyzer;
    }

    /**
     * Method called by lucence during indexing operations
     * 
     * @character fieldName the field name that the lucene object is currently processing
     * @character reader a Reader object to the index stream
     * 
     * @return an analyzer to specific to the field name or the default analyzer if none is present
     */
    public TokenStream tokenStream( String fieldName, Reader reader )
    {
        TokenStream tokenStream;

        if ( "version".equals( fieldName ) )
        {
            tokenStream = new VersionTokenizer( reader );
        }
        else
        {
            tokenStream = defaultAnalyzer.tokenStream( fieldName, reader );
        }

        return tokenStream;
    }
    
    /**
     * Class used to tokenize an artifact's version.
     */
    private class VersionTokenizer
        extends CharTokenizer
    {
        /**
         * Constructor with the required reader to the index stream
         *
         * @reader the Reader object of the index stream
         */
        public VersionTokenizer( Reader reader )
        {
            super( reader );
        }

        /**
         * method that lucene calls to check tokenization of a stream character
         * 
         * @param character char currently being processed
         *
         * @return true if the char is a token, false if the char is a stop char
         */
        protected boolean isTokenChar( char character )
        {
            boolean token;

            switch( character )
            {
                case '.': 
                case '-': 
                    token = false; break;
                default:
                    token = true;
            }

            return token;
        }
    }
}
