//
//  StatsObject.m
//  hoodyoodoo
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import "StatsObject.h"

@implementation StatsObject

@synthesize totalUsers, totalCelebrities, totalRatings, yourRatings;

- (id)init {
    self = [super init];
    if (self) {
    }
    return self;
}

- (NSString*) description {
    return [[NSString alloc]
            initWithFormat:@"StatsObject[totalUsers[%d] totalCelebrities[%d] totalRatings[%d] yourRatings[%d]]",
            [self totalUsers], [self totalCelebrities], [self totalRatings], [self yourRatings]];
}

@end
