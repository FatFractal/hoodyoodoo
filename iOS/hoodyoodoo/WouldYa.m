//
//  WouldYa.m
//  hoodyadoo
//
//  Created by Kevin Nickels on 1/10/12.
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import "WouldYa.h"

@implementation WouldYa

@synthesize selectedGuid, rejectedGuid;

- (id)init {
   self = [super init];
   if (self) {
   }
   return self;
}

- (NSString*) description {
   return [[NSString alloc]
           initWithFormat:@"WouldYa[selectedGuid[%@] rejectedGuid[%@]]",
           [self selectedGuid], [self rejectedGuid]];
}

@end
