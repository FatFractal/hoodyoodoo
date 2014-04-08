//
//  Celebrity.m
//  hoodyadoo
//
//  Created by Kevin Nickels on 1/10/12.
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import "Celebrity.h"

@implementation Celebrity

@synthesize firstName, lastName, imageData, gender, selectedCount, rejectedCount;

- (id)init {
   self = [super init];
   return self;
}

- (NSString*) description {
    return [[NSString alloc]
            initWithFormat:@"Celebrity[firstName[%@], lastName[%@], imageData[%d], gender[%@], selectedCount[%@], rejectedCount[%@]]",
            [self firstName],
            [self lastName],
            [[self imageData] length],
            [self gender],
            [self selectedCount],
            [self rejectedCount]
            ];
}
@end
