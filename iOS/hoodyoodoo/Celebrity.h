//
//  Celebrity.h
//  hoodyadoo
//
//  Created by Kevin Nickels on 1/10/12.
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//
/*! \brief a Celebrity object */
/*! The abstract parent of all Celebrity objects. */

@interface Celebrity : NSObject {
    /*! The NSString that contains the First Name for this Celebrity */
   NSString        *firstName;
    /*! The NSString that contains the Last Name for this Celebrity */
   NSString        *lastName;
    /*! The NSData for the profile image for this Celebrity */
   NSData          *imageData;
    /*! The NSString that contains the gender (male or female) for this Celebrity */
   NSString        *gender;
    /*! The NSNumber that contains the total times the Celebrity was selected.*/
    NSNumber       *selectedCount;
    /*! The NSNumber that contains the total times the Celebrity was rejected.*/
    NSNumber       *rejectedCount;
    
}

@property (strong, nonatomic) NSString          *firstName;
@property (strong, nonatomic) NSString          *lastName;
@property (strong, nonatomic) NSData            *imageData;
@property (strong, nonatomic) NSString          *gender;
@property (strong, nonatomic) NSNumber          *selectedCount;
@property (strong, nonatomic) NSNumber          *rejectedCount;

@end

