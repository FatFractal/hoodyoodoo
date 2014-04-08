//
//  CelebrityViewController.h
//  hoodyoodoo
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Celebrity.h"
#import "AppDelegate.h"

/*! \brief The CelebrityViewController class */

/*!
 * The CelebrityViewController interface with that will hold a UIImagePickerController 
 * that is used to select an image for the Celebrity.
 @attribute UIImagePickerController picker is used to select an image for the Celebrity.
 */
@interface CelebrityViewController : UIViewController {
    UIImagePickerController *picker;
}

/*! The #celebrity property is the Celebrity to be CRUD Created from the CelebrityViewController.*/
@property (strong, nonatomic) Celebrity *celebrity;

/*! The #firstNameField property is the UITextField that will capture the first name for the Celebrity.*/
@property (nonatomic, retain) IBOutlet UITextField *firstNameField;

/*! The #lastNameField property is the UITextField that will capture the last name for the Celebrity.*/
@property (nonatomic, retain) IBOutlet UITextField *lastNameField;

/*! The #doneButton property is the UIBarButtonItem that attempts calls the #addImage method.*/
@property (nonatomic, retain) IBOutlet UIBarButtonItem *doneButton;

/*! The #selectImageButton property is the UIButton that displays the image for the Celibrity and calls the #addImage method.*/
@property (nonatomic, retain) IBOutlet UIButton *selectImageButton;

/*! 
 * The #doneAction method handles the #doneButton click event, calls resignFirstResponder and if #firstNameField or 
 * #lastNameField contain text, will set Celebrity.firstName and/ or Celebrity.lastName.
 */
- (IBAction) doneAction:(id)sender;

/*! 
 * The #addImage method handles the #selectImageButton click event and calls a UIImagePickerController 
 * #imagePickerController:didFinishPickingMediaWithInfo: to choose an  * image from the device for the Celebrity for 
 * #celebrity and for the backgound image of the #selectImageButton.
 */
- (IBAction) addImage;

/*! The #imagePickerController:didFinishPickingMediaWithInfo: is used to select an image, set Celebrity.imageData to 
 * for #celebrity and sets the background image for #selectImageButton.
 */
- (void) imagePickerController:(UIImagePickerController *) Picker didFinishPickingMediaWithInfo:(NSDictionary *)info;

/*! The #imagePickerControllerDidCancel method handles the UIImagePickerController cancel event.*/
- (void) imagePickerControllerDidCancel:(UIImagePickerController *) Picker;

/*! 
 * The #addCelebrity method checks that all requisite information is present after a #doneButton click, calls 
 * AppDelegate::showLoginWithDelegate:action:message: if the user is not logged in and if logged in, calls 
 * FatFractal::createObj:atUrl: CRUD Create a new Celebrity to the ff noserver backend.
 */
- (void) addCelebrity;

/*! 
 * The #addGender method handles the #selectImageButton click event and calls a UIAlertView to select a gender for the Celebrity,
 * and set Celebrity.gender for #celebrity. Cancel will dismiss the UIAlertView without Celebrity.gender being set.
 */
- (void) addGender;

@end
