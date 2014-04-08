//
//  hoodyoodooTests.h
//  hoodyoodooTests
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

/**
 * \brief hoodyoodooTests class. 
 */

/** 
 The hoodyoodooTests class exercises the FatFractal backend. In Module 1, we cover 
 basic CRUD functionality.
 */
#import <SenTestingKit/SenTestingKit.h>
#import <FFEF/FatFractal.h>

/*!
 * The hoodyoodooTests interface with that will hold FatFractal instance for hoodyoodooTests.
 */
@interface hoodyoodooTests : SenTestCase {
    FatFractal * ff;
}

/*!
 * The test_100_SyncLogin test authenticates "test_user" using a synchronous method.
 */
- (void) test_100_SyncLogin;

/*!
 * The test_110_AsyncLogin test authenticates "test_user" using an asynchronous method.
 */
- (void) test_110_AsyncLogin;

/*!
 * Create a new Celebrity object. Note this method is synchronous.
 */
- (void)test_200_CreateCelebrity;

/*!
 * Create a new Celebrity object. Note this method is synchronous with error.
 *
 */
- (void)test_201_CreateCelebrity;

/*!
 * Create a new Celebrity object. Note this method is asynchronous.
 *
 */
- (void)test_202_CreateCelebrity;

/*!
 * Retrieve a Celebrity object. This test will first Create a Celebrity using an asynchronous method, then 
 retrieve is with an asynchronous methos, compare the two to verify that they are equal.
 */
- (void)test_210_ReadCelebrity;

/*!
 * Update a Celebrity object. This test will first Create a Celebrity using a synchronous method. It then 
 * modifies the celebrity object, Updates it to the backend with a synchronous method, compare the two to verify 
 * that they are not equal. It also checks to see that the version for the object has been incremented.
 */
- (void)test_220_UpdateCelebrity;

/*!
 * Update a Celebrity object. This test will first Create a Celebrity using a synchronous method. It then 
 * modifies the celebrity object, Updates it to the backend with a synchronous method, compare the two to verify 
 * that they are not equal. It also checks to see that the version for the object has been incremented.
 */
- (void)test_230_DeleteCelebrity;

/*!
 * The <b>setUp</b> method initializes the FatFractal class, sets the debug mode and calls authenticate.
 */
- (void)setUp;

- (void) syncAuthenticate;


@end
