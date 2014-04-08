//
//  CelebrityViewController.m
//  hoodyoodoo
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import "CelebrityViewController.h"
#import <QuartzCore/QuartzCore.h>

@implementation CelebrityViewController

@synthesize doneButton, selectImageButton, firstNameField, lastNameField, celebrity;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        celebrity = [[Celebrity alloc]init];
        [selectImageButton setBackgroundImage:nil forState:UIControlStateNormal];
        [selectImageButton setTitle:@"Add Image" forState:UIControlStateNormal];
        firstNameField.text = nil;
        lastNameField.text = nil;
    }
    return self;
}

/*
 * The addImage method brings up the UIImagePickerController
 */
-(IBAction) addImage {
    picker = [[UIImagePickerController alloc] init];
    picker.delegate = (id)self;   
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
        picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    } else {
        picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    }
    [self presentModalViewController:picker animated:YES];
}

/*
 * The imagePickerController didFinishPickingMediaWithInfo method will get the image, add it to the UIButton selectImageButton, clear 
 * the title and, most importantly, add the image data to the Celebrity object.
 */
- (void)imagePickerController:(UIImagePickerController *) Picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    UIImage *celebrityImage = [info objectForKey:UIImagePickerControllerOriginalImage];
    if(celebrityImage != nil) {
        [selectImageButton setBackgroundImage:celebrityImage forState:UIControlStateNormal];
        [selectImageButton setTitle:@"" forState:UIControlStateNormal];
        celebrity.imageData = UIImagePNGRepresentation(celebrityImage);
    }
    [self dismissModalViewControllerAnimated:YES];
}

/*
 * The imagePickerControllerDidCancel method just dismisses the UIImagePickerController. 
 */
- (void)imagePickerControllerDidCancel:(UIImagePickerController *) Picker {
    [self dismissModalViewControllerAnimated:YES];
}

-(void)addGender {
    UIAlertView *genderPicker = [[UIAlertView alloc] 
                                 initWithTitle:@"Gender"
                                 message:@" Please select the gender for this Celebrity?"
                                 delegate:self 
                                 cancelButtonTitle:@"Male"otherButtonTitles:@"Female",nil];
    [genderPicker show];    
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    NSLog(@"CelebrityViewController alertView got here.");
    [self resignFirstResponder];
    if([[alertView buttonTitleAtIndex:buttonIndex] isEqualToString:@"Male"]) {
        celebrity.gender = @"male";
        NSLog(@"CelebrityViewController genderPicker: celebrity.gender set to male.");
    }
    else if([[alertView buttonTitleAtIndex:buttonIndex] isEqualToString:@"Female"]) {
        celebrity.gender = @"female";
        NSLog(@"CelebrityViewController genderPicker: celebrity.gender set to female.");
    } else  {
        NSLog(@"Something bad happened");
    }
    [self addCelebrity];
}

-(IBAction)doneAction:(id)sender {
    // dismiss keyboard
    [firstNameField resignFirstResponder];
	[lastNameField resignFirstResponder];
    if(firstNameField.text.length > 0) celebrity.firstName = firstNameField.text;
    if(lastNameField.text.length > 0) celebrity.lastName = lastNameField.text;
    [self addCelebrity];
}

- (void)addCelebrity {
    // check logged in
    if(![[FatFractal main] loggedIn]) {
        [(AppDelegate *)[[UIApplication sharedApplication] delegate] 
         showLoginWithDelegate:self action:@selector(addCelebrity) 
         message:@"Please Login"];
    }
    else {
        // check requisite info exists
        Celebrity *newCelebrity = [[Celebrity alloc] init];
        newCelebrity.firstName = @"Sean";
        newCelebrity.lastName = @"Connery";        
        if ((celebrity.firstName == nil) || (celebrity.lastName == nil)) {
            UIAlertView *failview = [[UIAlertView alloc] 
                                     initWithTitle:@"Add Celebrity Failed"
                                     message:@"You must provide first and last name for this celebrity"
                                     delegate:nil 
                                     cancelButtonTitle:@"OK" 
                                     otherButtonTitles:nil];
            [failview show];
        }
        else if (celebrity.imageData == nil) {
            UIAlertView *failview = [[UIAlertView alloc] 
                                     initWithTitle:@"Add Celebrity Failed"
                                     message:@"You must provide an image for this celebrity"
                                     delegate:nil 
                                     cancelButtonTitle:@"OK" 
                                     otherButtonTitles:nil];
            [failview show];
        } 
        else if (celebrity.gender == nil) {
            [self addGender];
        } 
        else {
            NSLog(@"CelebrityViewController createCelebrity: all requisite items exist.");
            NSLog(@"CelebrityViewController createCelebrity newCelebString = %@", [celebrity description]);
            NSError *error;
            [[FatFractal main] createObj:celebrity atUrl:@"/Celebrity" error:&error];
            if(error) {
                NSLog(@"CelebrityViewController addCelebrity failed: %@", [error localizedDescription]);
            } else {
                [selectImageButton setBackgroundImage:[UIImage imageNamed:@"addphoto.png"] forState:UIControlStateNormal];
                [selectImageButton setTitle:@"Add Image" forState:UIControlStateNormal];
                firstNameField.text = nil;
                lastNameField.text = nil;
                celebrity = nil;
                celebrity = [[Celebrity alloc]init];
            }
        }
    }
}

#pragma mark - View lifecycle

/*
 // Implement loadView to create a view hierarchy programmatically, without using a nib.
 - (void)loadView
 {
 }
 */

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    /* 
     Initialize the celebrity
     */
    NSLog(@"got here");
    celebrity = [[Celebrity alloc]init];
    [selectImageButton setBackgroundImage:[UIImage imageNamed:@"addphoto.png"] forState:UIControlStateNormal];
    [selectImageButton setTitle:@"Add an image from your photo library or one saved from some website" forState:UIControlStateNormal];
    [[selectImageButton layer] setCornerRadius:16.0f];
    [[selectImageButton layer] setMasksToBounds:YES];    
    [[selectImageButton layer] setBorderColor:[UIColor greenColor].CGColor];
    [[selectImageButton layer] setBorderWidth:4.0f];
    firstNameField.text = nil;
    lastNameField.text = nil;
}

- (void)viewWillAppear:(BOOL)animated {
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
