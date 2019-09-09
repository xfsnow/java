package com.aws;

import com.aws.aws.S3Client;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args )
    {
        System.out.println( "Hello World from Maven!" );
        S3Client.listBucket();
    }
}
