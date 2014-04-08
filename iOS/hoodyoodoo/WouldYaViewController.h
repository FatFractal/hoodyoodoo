//
//  WouldYaViewController.h
//  hoodyoodoo
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Celebrity.h"
#import "AppDelegate.h"

/*! \brief The WouldYaViewController class */

@interface WouldYaViewController : UIViewController

/*! The leftCelebrity property is the first random Celebrity to be CRUD Retrieved for the WouldYaViewController.*/
@property (strong, nonatomic) Celebrity *leftCelebrity;

/*!
 * The leftCelebrityButton property is the UIButton that will display the profile image of the
 * top Celebrity and calls celebrityWasPicked when clicked.
 */
@property (strong, nonatomic) IBOutlet UIButton    *leftCelebrityButton;

/*!
 * The leftCelebrityLabel property is the UILabel that will display the concatenated first and last
 * name of the top Celebrity.
 */
@property (strong, nonatomic) IBOutlet UILabel     *leftCelebrityLabel;

/*!
 * The loadCelebrities method does a CRUD Retrieve to populate celebrityList and leftCelebrity for
 * the WouldYaViewController.
 */
- (void) loadCelebrities;

/*!
 * The celebrityWasPicked method handles clicks from leftCelebrityButton or rightCelebrityButton and does  
 * a CRUD Create of a new WouldYa to the back-end.
 */
- (IBAction)celebrityWasPicked:(id)sender;

@end
