//
//  WouldYaViewController.m
//  hoodyoodoo
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import "WouldYaViewController.h"
#import <QuartzCore/QuartzCore.h>

@implementation WouldYaViewController

@synthesize leftCelebrity, leftCelebrityLabel, leftCelebrityButton;
@synthesize rightCelebrity, rightCelebrityLabel, rightCelebrityButton, currentGender, genderButton, playAgainButton, wouldYa;

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

- (void) loadCelebrities {
    playAgainButton.hidden = YES;
    playAgainButton.enabled = NO;
    [leftCelebrityButton setImage:nil forState:UIControlStateNormal];
    [rightCelebrityButton setImage:nil forState:UIControlStateNormal];
    [[leftCelebrityButton layer] setBorderColor:[UIColor grayColor].CGColor];
    [[rightCelebrityButton layer] setBorderColor:[UIColor grayColor].CGColor];
    NSLog(@"\n\nloadCelebrities\n\n");
    [[FatFractal main] getObjFromUrl:[NSString stringWithFormat:
                                      @"/ff/resources/Celebrity/(gender eq '%@' and guid eq random(guid))",
                                      currentGender]
                          onComplete:^(NSError *err, id celeb, NSHTTPURLResponse *httpResponse) 
     {   
         if (err) {
             NSLog(@"WouldYaViewController loadCelebrities leftCelebrity failed: %@", [err localizedDescription]);
             [leftCelebrityButton setBackgroundImage:[UIImage imageNamed:@"mystery2.png"] forState:UIControlStateNormal];
             leftCelebrityLabel.text = @"No celebrities found";
             [rightCelebrityButton setBackgroundImage:[UIImage imageNamed:@"mystery3.png"] forState:UIControlStateNormal];
             rightCelebrityLabel.text = @"No celebrities found";
             return;
         } 
         if(celeb) {
             leftCelebrity = celeb;
             NSLog(@"loadCelebrities leftCelebrity: %@", [leftCelebrity description]);
             FFMetaData *meta = [[FatFractal main] metaDataForObj:celeb];
             NSLog(@"%@", [meta description]);
             leftCelebrityLabel.text = [NSString stringWithFormat:@"%@ %@", leftCelebrity.firstName, leftCelebrity.lastName];
             [leftCelebrityButton setBackgroundImage:[[UIImage alloc] initWithData:leftCelebrity.imageData] forState:UIControlStateNormal];
             NSError * error;             
             rightCelebrity = [[FatFractal main] 
                                getObjFromUrl:[NSString stringWithFormat:
                                               @"/ff/resources/Celebrity/(gender eq '%@' and guid ne '%@' and guid eq random(guid))",
                                               currentGender,
                                               [[[FatFractal main] metaDataForObj:leftCelebrity] guid]]
                                error:&error];
             if (error) {
                 NSLog(@"WouldYaViewController loadCelebrities rightCelebrity failed: %@", [error localizedDescription]);
                 [rightCelebrityButton setBackgroundImage:[UIImage imageNamed:@"genericfriendicon.png"] forState:UIControlStateNormal];
                 rightCelebrityLabel.text = @"No celebrities found";
                 return;
             } 
             NSLog(@"loadCelebrities: rightCelebrity = %@", rightCelebrity);    
             if(rightCelebrity) {
                 NSLog(@"loadCelebrities rightCelebrity: %@", [rightCelebrity description]);
                 FFMetaData *meta = [[FatFractal main] metaDataForObj:rightCelebrity];
                 NSLog(@"%@", [meta description]);
                 rightCelebrityLabel.text = [NSString stringWithFormat:@"%@ %@", rightCelebrity.firstName, rightCelebrity.lastName];
                 [rightCelebrityButton setBackgroundImage:[[UIImage alloc] initWithData:rightCelebrity.imageData] forState:UIControlStateNormal];
             } else {
                 NSLog(@"WouldYaViewController loadCelebrities rightCelebrity not found.");
                 [rightCelebrityButton setBackgroundImage:[UIImage imageNamed:@"genericfriendicon.png"] forState:UIControlStateNormal];
                 rightCelebrityLabel.text = @"No celebrities found";
             }
         } else {
             NSLog(@"WouldYaViewController loadCelebrities leftCelebrity not found.");
             [leftCelebrityButton setBackgroundImage:[UIImage imageNamed:@"genericfriendicon.png"] forState:UIControlStateNormal];
             leftCelebrityLabel.text = @"No celebrities found";
         }
     }];
}

- (IBAction)toggleGender:(id)sender {
    if([self.currentGender isEqualToString:@"male"]) {
        [genderButton setBackgroundImage:[UIImage imageNamed:@"button_gender_female.png"] forState:UIControlStateNormal];
        self.currentGender = @"female";
    } else {
        [genderButton setBackgroundImage:[UIImage imageNamed:@"button_gender_male.png"] forState:UIControlStateNormal];
        self.currentGender = @"male";
    }
    [self loadCelebrities];
    
}

- (IBAction)celebrityWasPicked:(id)sender {
    if(sender == leftCelebrityButton) {
        wouldYa.selectedGuid = [[[FatFractal main] metaDataForObj:leftCelebrity] guid];
        wouldYa.rejectedGuid = [[[FatFractal main] metaDataForObj:rightCelebrity] guid];
        //[leftCelebrityButton setImage:[UIImage imageNamed:@"genericfriendicon.png"] forState:UIControlStateNormal];
        [[leftCelebrityButton layer] setBorderColor:[UIColor greenColor].CGColor];
        [[rightCelebrityButton layer] setBorderColor:[UIColor redColor].CGColor];
    } else if(sender == rightCelebrityButton) {
        wouldYa.selectedGuid = [[[FatFractal main] metaDataForObj:rightCelebrity] guid];
        wouldYa.rejectedGuid = [[[FatFractal main] metaDataForObj:leftCelebrity] guid];
        //[rightCelebrityButton setImage:[UIImage imageNamed:@"genericfriendicon.png"] forState:UIControlStateNormal];
        [[leftCelebrityButton layer] setBorderColor:[UIColor redColor].CGColor];
        [[rightCelebrityButton layer] setBorderColor:[UIColor greenColor].CGColor];
    }
    if(wouldYa) {
        [self persistWouldYa];
    }
    else NSLog(@"WouldYaViewController celebrityWasPicked failed: wouldYa is nil");
}

- (void)persistWouldYa {
    if(![[FatFractal main] loggedIn]) {
        [(AppDelegate *)[[UIApplication sharedApplication] delegate]
         showLoginWithDelegate:self action:@selector(persistWouldYa) 
         message:@"Please Login"];
    } else {
        playAgainButton.enabled = YES;
        playAgainButton.hidden = NO;
        NSError *error;
        if(wouldYa) { [[FatFractal main] createObj:wouldYa atUrl:@"/WouldYa" error:&error];
            if(error) NSLog(@"WouldYaViewController persistWouldYa failed: %@", [error localizedDescription]);
            else {
                wouldYa = nil;
                wouldYa = [[WouldYa alloc]init];
            }
        } else NSLog(@"WouldYaViewController persistWouldYa failed: wouldYa is nil");
    }    
}

- (IBAction)playAgain:(id)sender {
    [self loadCelebrities];
}

#pragma mark - View lifecycle

- (void)viewDidLoad {
    [super viewDidLoad];
    self.wantsFullScreenLayout = YES;
    [genderButton setBackgroundImage:[UIImage imageNamed:@"button_gender_female.png"] forState:UIControlStateNormal];
    currentGender = @"female";
    [[leftCelebrityButton layer] setCornerRadius:16.0f];
    [[leftCelebrityButton layer] setMasksToBounds:YES];    
    [[leftCelebrityButton layer] setBorderColor:[UIColor grayColor].CGColor];
    [[leftCelebrityButton layer] setBorderWidth:4.0f];
    [[rightCelebrityButton layer] setCornerRadius:16.0f];
    [[rightCelebrityButton layer] setMasksToBounds:YES];    
    [[rightCelebrityButton layer] setBorderColor:[UIColor grayColor].CGColor];
    [[rightCelebrityButton layer] setBorderWidth:4.0f];
    [leftCelebrityButton setBackgroundImage:[UIImage imageNamed:@"mystery2.png"] forState:UIControlStateNormal];
    [rightCelebrityButton setBackgroundImage:[UIImage imageNamed:@"mystery3.png"] forState:UIControlStateNormal];
    wouldYa = [[WouldYa alloc]init];
}

- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self loadCelebrities];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}
@end
