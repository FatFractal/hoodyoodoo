//
//  WouldYa.h
//  hoodyadoo
//
//  Created by Kevin Nickels on 1/10/12.
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

@interface WouldYa : NSObject {
   NSString        *selectedGuid;
   NSString        *rejectedGuid;
}

@property (strong, nonatomic) NSString          *selectedGuid;
@property (strong, nonatomic) NSString          *rejectedGuid;

@end
