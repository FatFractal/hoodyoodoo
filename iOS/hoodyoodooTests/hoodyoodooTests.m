//
//  hoodyoodooTests.m
//  hoodyoodooTests
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//
/*! \brief hoodyoodooTests implementation. */
/*! 
 The hoodyoodooTests class exercises the FatFractal back-end.
 */

#import "hoodyoodooTests.h"
#import "Celebrity.h" // what we want to make sure will work.

/*!
 The #baseUrl property (NSString) of the FatFractal helper class allows the developer to 
 change the target host and application name for non-SSL without having to change all of 
 the CRUD, query, server extension and event methods. For example, you could set baseUrl 
 to something like: http://acme.fatfractal.com/hoodyoodoo
 */
static NSString * baseUrl = @"http://acme.fatfractal.com/hoodyoodoo";


/*!
 The <b>sslUrl</b> is the url and optional port that is used by all methods in hoodyoodooTests 
 that do not use SSL.
 */
static NSString * sslUrl  = @"https://acme.fatfractal.com/hoodyoodoo";

@implementation hoodyoodooTests

- (void) syncAuthenticate {    
    FFUser * loggedInUser = [ff loginWithUserName:@"test_user" andPassword:@"test_user"];
    STAssertNotNil(loggedInUser, @"Login failed");
}

/*!
 The <b>asyncAuthenticate</b> method will login the user and autregister the user if the noserver application is configured to 
 allow that in the fatfractal.xml that is generated during scaffolding (AllowAutoRegistration = YES).
 */
- (void) asyncAuthenticate {
    NSLog(@"Logging in");
    __block BOOL calledBack = NO;
    [ff loginWithUserName:@"test_user" andPassword:@"test_user" onComplete:^(NSError *err, id ffUser, NSHTTPURLResponse *httpResponse) {
        NSLog(@"called back");
        calledBack = YES;
        STAssertNil(err, @"authenticate failed: error: %@", [err localizedDescription]);
    }];
    while (!calledBack) {
        NSDate* cycle = [NSDate dateWithTimeIntervalSinceNow:0.5];
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode
                                 beforeDate:cycle];
    }
}

/*!
 The <b>setUp</b> method initializes the FatFractal class, sets the debug mode and calls authenticate.
 */
- (void)setUp {
    [super setUp];    
    // Set-up code here.
    if (! ff) {
        ff = [[FatFractal alloc] initWithBaseUrl:baseUrl sslUrl:sslUrl];
    }
    [FFHttpDelegate addTrustedHost:@"localhost"];
    [ff setDebug:true];
    [self syncAuthenticate];
}

- (void) deleteAllCelebritys {
    [self syncAuthenticate];
    NSError *readError;
    NSArray *myCelebrities = [[FatFractal main]
                              getArrayFromUrl:[NSString stringWithFormat:@"/ff/resources/Celebrity/(createdBy eq 'test_user')"]
                              error:&readError];
    STAssertNil(readError, @"deleteAllCelebritys failed to retrieve my test_user Celebrity objects with error %@", readError);
    if (! readError) {
        NSError *deleteError;
        for (id obj in myCelebrities) {
            [ff deleteObj:obj error:&deleteError];
            STAssertNil(deleteError, @"deleteAllCelebritys failed to retrieve my test_user Celebrity objects with error %@", deleteError);
        }
    }
}

- (void)tearDown {
    [super tearDown];
}

/*!
 * The test_Sync_200_Login test authenticates "test_user" using a synchronous method.
 *
 */
- (void) test_100_SyncLogin {
    [self syncAuthenticate];
}

/*!
 * The test_Async_200_Login test authenticates "test_user" using an asynchronous method.
 *
 */
- (void) test_110_AsyncLogin {
    [self asyncAuthenticate];
}

/*!
 * Create a new Celebrity object. Note this method is synchronous.
 *
 */
- (void)test_200_CreateCelebrity {
    [self syncAuthenticate];
    Celebrity * newCelebrity = [[Celebrity alloc] init];
    newCelebrity.firstName = @"Milla";
    newCelebrity.lastName = @"Jovovich";
    newCelebrity.gender = @"female";
    newCelebrity.imageData = UIImagePNGRepresentation([UIImage imageNamed:@"mystery2.png"]);
    NSLog(@"\r\n\tnewCelebrity Data: %@, \r\n\tMetadata: %@", [newCelebrity description], [[ff metaDataForObj:newCelebrity] description]);
    Celebrity * createdCelebrity = [ff createObj:newCelebrity atUrl:@"/Celebrity"];
    STAssertNotNil(createdCelebrity, @"Celebrity createObj failed.");
    NSLog(@"\r\n\tcreatedCelebrity Data: %@, \r\n\tMetadata: %@", [createdCelebrity description], [[ff metaDataForObj:createdCelebrity] description]);
}

/*!
 * Create a new Celebrity object. Note this method is synchronous with error.
 *
 */
- (void)test_201_CreateCelebrity {
    [self syncAuthenticate];
    Celebrity * newCelebrity = [[Celebrity alloc] init];
    newCelebrity.firstName = @"Milla";
    newCelebrity.lastName = @"Jovovich";
    newCelebrity.gender = @"female";
    newCelebrity.imageData = UIImagePNGRepresentation([UIImage imageNamed:@"mystery2.png"]);
    NSLog(@"\r\n\tnewCelebrity Data: %@, \r\n\tMetadata: %@", [newCelebrity description], [[ff metaDataForObj:newCelebrity] description]);
    NSError *createError;
    Celebrity * createdCelebrity = [ff createObj:newCelebrity atUrl:@"/Celebrity" error:&createError];
    STAssertNotNil(createdCelebrity, @"Celebrity createObj failed: %@", [createError localizedDescription]);
    STAssertNil(createError, @"Celebrity createObj failed: %@", [createError localizedDescription]);
    NSLog(@"\r\n\tcreatedCelebrity Data: %@, \r\n\tMetadata: %@", [createdCelebrity description], [[ff metaDataForObj:createdCelebrity] description]);
}

/*!
 * Create a new Celebrity object. Note this method is asynchronous.
 *
 */
- (void)test_202_CreateCelebrity {
    [self syncAuthenticate];
    Celebrity * newCelebrity = [[Celebrity alloc] init];
    newCelebrity.firstName = @"Milla";
    newCelebrity.lastName = @"Jovovich";
    newCelebrity.gender = @"female";
    newCelebrity.imageData = UIImagePNGRepresentation([UIImage imageNamed:@"mystery2.png"]);
    NSLog(@"\r\n\tnewCelebrity Data: %@, \r\n\tMetadata: %@", [newCelebrity description], [[ff metaDataForObj:newCelebrity] description]);
    __block BOOL testCompleted = NO;
    [ff createObj:newCelebrity atUrl:@"/Celebrity" onComplete:^(NSError *createError, id createdCelebrity, NSHTTPURLResponse *httpResponse) {
        STAssertNil(createError, @"test_210_ReadCelebrity createObj failed: %@", [createError localizedDescription]);
        if (createError) return;
        NSLog(@"\r\n\tcreatedCelebrity Data: %@, \r\n\tMetadata: %@", [createdCelebrity description], [[ff metaDataForObj:createdCelebrity] description]);
        NSString *guid = [[ff metaDataForObj:createdCelebrity] guid];
        STAssertNotNil(createdCelebrity, @"Celebrity createObj failed: %@", [createError localizedDescription]);
        STAssertNotNil(guid, @"test_210_ReadCelebrity createObj failed, guid is null: %@", guid);
        testCompleted = YES;
    }];
    while (!testCompleted) {
        NSDate* cycle = [NSDate dateWithTimeIntervalSinceNow:0.01];
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode
                                 beforeDate:cycle];
    }
}

/*!
 * Read a Celebrity object. This test will first Create a Celebrity using an asynchronous method, then 
 read it with an asynchronous method, compare the two to verify that they are equal.
 *
 */
- (void)test_210_ReadCelebrity {
    [self syncAuthenticate];
    Celebrity * newCelebrity = [[Celebrity alloc] init];
    newCelebrity.firstName = @"Kate";
    newCelebrity.lastName = @"Beckinsdale";
    newCelebrity.gender = @"female";
    newCelebrity.imageData = UIImagePNGRepresentation([UIImage imageNamed:@"mystery2.png"]);
    NSLog(@"\r\n\tnewCelebrity Data: %@, \r\n\tMetadata: %@", [newCelebrity description], [[ff metaDataForObj:newCelebrity] description]);    
    __block BOOL testCompleted = NO;
    [ff createObj:newCelebrity atUrl:@"/Celebrity" onComplete:^(NSError *createError, id createdCelebrity, NSHTTPURLResponse *httpResponse) {
        STAssertNil(createError, @"test_210_ReadCelebrity createObj failed: %@", [createError localizedDescription]);
        if (createError) return;
        NSLog(@"\r\n\tcreatedCelebrity Data: %@, \r\n\tMetadata: %@", [createdCelebrity description], [[ff metaDataForObj:createdCelebrity] description]);
        NSString *guid = [[ff metaDataForObj:createdCelebrity] guid];
        STAssertNotNil(guid, @"test_210_ReadCelebrity createObj failed, guid is null: %@", guid);        
        NSString *ffUrl = [[ff metaDataForObj:createdCelebrity] ffUrl];
        [ff getObjFromUrl:ffUrl onComplete:^(NSError *readError, id readCelebrity, NSHTTPURLResponse *httpResponse) {
            STAssertNil(readError, @"test_210_ReadCelebrity getObjFromUrl %@ failed: %@", ffUrl, [readError localizedDescription]);
            NSString *reason;
            BOOL readEqualsCreated = [FFUtils object:createdCelebrity hasEqualValuesTo:readCelebrity notEqualReason:&reason];            
            STAssertTrue(readEqualsCreated, reason);
            NSLog(@"\r\n\treadCelebrity Data: %@, \r\n\tMetadata: %@", [readCelebrity description], [[ff metaDataForObj:readCelebrity] description]);
            testCompleted = YES;
        }];
    }];
    while (!testCompleted) {
        NSDate* cycle = [NSDate dateWithTimeIntervalSinceNow:0.01];
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode
                                 beforeDate:cycle];
    }
}

/*!
 * Read a Celebrity object expecting no items returned. This test will first delete all Celebritys using the private 
 * deleteAllCelebritys method, then read it with an asynchronous method, and verify no results returned.
 */
- (void)test_211_ReadNoCelebritys {
    [self syncAuthenticate];
    [self deleteAllCelebritys];
    NSString * queryUrl = @"/ff/resources/Celebrity/(createdBy eq 'test_user' and guid eq random(guid))";
    NSError *readError;
    NSArray * readCelebrities = [ff getArrayFromUrl:queryUrl error:&readError];
    STAssertNil(readError, @"test_210_ReadCelebrity getObjFromUrl %@ failed: %@", queryUrl, [readError localizedDescription]);
    STAssertTrue(readCelebrities.count == 0, @"test_211_ReadNoCelebritys getObjFromUrl %@ failed, should be nil, instead got: %@", queryUrl, readCelebrities);
}

/*!
 * Update a Celebrity object. This test will first Create a Celebrity using a synchronous method. It then 
 * modifies the celebrity object, Updates it to the backend with a synchronous method, compare the two to verify 
 * that they are not equal. It also checks to see that the version for the object has been incremented.
 */
- (void)test_220_UpdateCelebrity {
    [self syncAuthenticate];
    Celebrity * newCelebrity = [[Celebrity alloc] init];
    newCelebrity.firstName = @"George";
    newCelebrity.lastName = @"Cloonee"; // oops, I made a mistake!
    newCelebrity.gender = @"male"; // oops, I made a mistake!
    newCelebrity.imageData = UIImagePNGRepresentation([UIImage imageNamed:@"mystery3.png"]);
    NSError *updateError;
    NSLog(@"\r\n\tData: %@, \r\n\tMetadata: %@", [newCelebrity description], [[ff metaDataForObj:newCelebrity] description]);
    Celebrity * createdCelebrity = [ff createObj:newCelebrity atUrl:@"/Celebrity" error:&updateError];
    STAssertNotNil(createdCelebrity, @"Celebrity createObj failed: %@", [updateError localizedDescription]);
    STAssertNil(updateError, @"Celebrity createObj failed: %@", [updateError localizedDescription]);
    createdCelebrity.lastName = @"Clooney";
    Celebrity * updatedCelebrity = [ff updateObj:createdCelebrity];
    NSLog(@"\r\n\tData: %@, \r\n\tMetadata: %@", [updatedCelebrity description], [[ff metaDataForObj:updatedCelebrity] description]);
    NSString *reason;
    BOOL createdEqualsUpdated = [FFUtils object:createdCelebrity hasEqualValuesTo:updatedCelebrity notEqualReason:&reason];
    STAssertTrue(createdEqualsUpdated, reason);                 
    int readVersion = [[[ff metaDataForObj:updatedCelebrity] objVersion] intValue];
    BOOL versionIsCorrect = (readVersion == 3); // since create with blob uses 2 pass write...
    STAssertTrue(versionIsCorrect, @"test_220_UpdateCelebrity Version should be 3, is %d", readVersion);
}

/*!
 * Delete a Celebrity object. This test will first Create a Celebrity using a synchronous method. It then 
 * Deletes the celebrity object, tries to read it and verifies that it could not be retreieved (404 error).
 */
- (void)test_230_DeleteCelebrity {
    [self syncAuthenticate];
    Celebrity * newCelebrity = [[Celebrity alloc] init];
    newCelebrity.firstName = @"George";
    newCelebrity.lastName = @"Clooney";
    newCelebrity.gender = @"male";
    newCelebrity.imageData = UIImagePNGRepresentation([UIImage imageNamed:@"mystery3.png"]);
    NSLog(@"\r\n\tData: %@, \r\n\tMetadata: %@", [newCelebrity description], [[ff metaDataForObj:newCelebrity] description]);
    __block BOOL testCompleted = NO;
    [ff createObj:newCelebrity atUrl:@"/Celebrity" onComplete:^(NSError *err, id createdCelebrity, NSHTTPURLResponse *httpResponse) {
        STAssertNil(err, @"test_230_DeleteCelebrity createObj failed: %@", [err localizedDescription]);
        if (err) return;
        [ff deleteObj:createdCelebrity onComplete:^(NSError *err, id deletedCelebrity, NSHTTPURLResponse *httpUrlResponse) {
            NSLog(@"\r\n\tcreatedCelebrity Data: %@, \r\n\tMetadata: %@", [deletedCelebrity description], [[ff metaDataForObj:deletedCelebrity] description]);
            NSLog(@"\r\n\tdeletedCelebrity Data: %@, \r\n\tMetadata: %@", [deletedCelebrity description], [[ff metaDataForObj:deletedCelebrity] description]);
            STAssertNil(err, @"test_230_DeleteCelebrity deleteObj failed: %@", [err localizedDescription]);    
            NSString *ffUrl = [[ff metaDataForObj:createdCelebrity] ffUrl];
            [ff getObjFromUrl:ffUrl onComplete:^(NSError *err, id obj, NSHTTPURLResponse *httpResponse) {
                BOOL got404 = ([httpResponse statusCode] == 404);
                STAssertTrue(got404, @"Should have received a 404, got a %d", [httpResponse statusCode]);
                if (err) NSLog(@"GET NonExistentExtension failed as expected");
                testCompleted = YES;
            }];
        }];
    }];
    while (!testCompleted) {
        NSDate* cycle = [NSDate dateWithTimeIntervalSinceNow:0.01];
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode
                                 beforeDate:cycle];
    }
}
/*!
 * Delete all Celebrity objects. 
 */
- (void)test_240_DeleteAllCelebritys {
    [self deleteAllCelebritys];
}


@end
