//
//  StatsObject.h
//  hoodyoodoo
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface StatsObject : NSObject {
    NSNumber        *totalUsers;
    NSNumber        *totalCelebrities;
    NSNumber        *totalRatings;
    NSNumber        *yourRatings;
}

@property (strong, nonatomic) NSNumber          *totalUsers;
@property (strong, nonatomic) NSNumber          *totalCelebrities;
@property (strong, nonatomic) NSNumber          *totalRatings;
@property (strong, nonatomic) NSNumber          *yourRatings;

@end
