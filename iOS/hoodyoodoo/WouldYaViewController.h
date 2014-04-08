//
//  WouldYaViewController.h
//  hoodyoodoo
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Celebrity.h"
#import "AppDelegate.h"
#import "WouldYa.h"

/*! \brief The WouldYaViewController class */

@interface WouldYaViewController : UIViewController

/*! The leftCelebrity property is the first random Celebrity to be CRUD Retrieved for the WouldYaViewController.*/
@property (strong, nonatomic) Celebrity *leftCelebrity;

/*! The rightCelebrity property is the second random Celebrity to be CRUD Retrieved for the WouldYaViewController.*/
@property (strong, nonatomic) Celebrity *rightCelebrity;

/*! The wouldYa property is the WouldYa to be CRUD Created for the WouldYaViewController.*/
@property (strong, nonatomic) WouldYa *wouldYa;

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
 * The rightCelebrityButton property is the UIButton that will display the profile image of the
 * bottom Celebrity and calls celebrityWasPicked when clicked.
 */
@property (strong, nonatomic) IBOutlet UIButton    *rightCelebrityButton;

/*!
 * The rightCelebrityLabel property is the UILabel that will display the concatenated first and last
 * name of the bottom Celebrity.
 */
@property (strong, nonatomic) IBOutlet UILabel     *rightCelebrityLabel;

/*!
 * The genderButton property is the UIButton that will set the gender game selection for the user.
 */
@property (strong, nonatomic) IBOutlet UIButton    *genderButton;

/*!
 * The playAgainButton property is an UIButton that will calls loadCelebrities to load two new Celebrities.
 */
@property (strong, nonatomic) IBOutlet UIButton *playAgainButton;

/*!
 * The currentGender property is an NSString that will holds the gender game selection for the user.
 */
@property (strong, nonatomic) NSString  *currentGender;

/*!
 * The loadCelebrities method does a CRUD Retrieve to populate celebrityList and leftCelebrity for
 * the WouldYaViewController.
 */
- (void) loadCelebrities;

/*!
 * The toggleGender method handles genderButton and sets currentGender for the WouldYaViewController.
 */
- (IBAction)toggleGender:(id)sender;

/*!
 * The celebrityWasPicked method handles clicks from leftCelebrityButton or rightCelebrityButton and does  
 * a CRUD Create of a new WouldYa to the back-end.
 */
- (IBAction)celebrityWasPicked:(id)sender;

/*!
 * The persistWouldYa method does a CRUD Create to persist wouldYa to the back-end
 */
- (void) persistWouldYa;

/*!
 * The playAgain method handles playAgainButton and calls the WouldYaViewController loadCelebrities method.
 */
- (IBAction)playAgain:(id)sender;

@end
