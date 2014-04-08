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

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

- (void) loadCelebrities {
    NSError * error;
    NSArray *celebArray = [[FatFractal main]
                           getArrayFromUrl:[NSString stringWithFormat:@"/ff/resources/Celebrity"]
                           error:&error];
    if (error) {
        NSLog(@"WouldYaViewController loadCelebrities celebArray failed: %@", [error localizedDescription]);
        return;
    }
    if([celebArray count] == 0) {
        NSLog(@"WouldYaViewController loadCelebrities no celebrities found");
        [leftCelebrityButton setBackgroundImage:[UIImage imageNamed:@"genericfriendicon.png"] forState:UIControlStateNormal];
        leftCelebrityLabel.text = @"No celebrities found";
        return;
    }
    int r = arc4random() % [celebArray count];
    leftCelebrity = [celebArray objectAtIndex:r];
    if(leftCelebrity) {
        leftCelebrityLabel.text = [NSString stringWithFormat:@"%@ %@", leftCelebrity.firstName, leftCelebrity.lastName];
        [leftCelebrityButton setBackgroundImage:[[UIImage alloc] initWithData:leftCelebrity.imageData] forState:UIControlStateNormal];
    } else {
        NSLog(@"WouldYaViewController loadCelebrities leftCelebrity could not find any");
    }
}


- (IBAction)celebrityWasPicked:(id)sender {
    [self loadCelebrities];
}


#pragma mark - View lifecycle

- (void)viewDidLoad {
    [super viewDidLoad];
    self.wantsFullScreenLayout = YES;
    [[leftCelebrityButton layer] setCornerRadius:16.0f];
    [[leftCelebrityButton layer] setMasksToBounds:YES];    
    [[leftCelebrityButton layer] setBorderColor:[UIColor grayColor].CGColor];
    [[leftCelebrityButton layer] setBorderWidth:4.0f];
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
