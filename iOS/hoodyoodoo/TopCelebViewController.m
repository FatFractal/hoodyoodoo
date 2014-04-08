//
//  TopCelebViewController.m
//  hoodyoodoo
//
//  Created by Kevin Nickels on 1/6/12.
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import "TopCelebViewController.h"
#import <QuartzCore/QuartzCore.h>

@implementation TopCelebViewController

@synthesize selectedLabel, rejectedLabel, topCeleb, celebrityLabel, topCelebImageView;
@synthesize statsObject, totalUsersLabel, totalRatingsLabel, totalCelebritiesLabel, yourRatingsLabel;

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

-(void) getTopCelebrity {
    NSError *error;
    NSArray *celebs = [[FatFractal main] getArrayFromUrl:@"/ff/resources/TopCelebrity" error:&error];
    if (error) {
        NSLog(@"StatsViewController getTopCelebrity failed: %@", [error localizedDescription]);
        celebrityLabel.text = @"No Top Celebrity found";             
        return;
    } 
    if([celebs count] > 0) {
        topCeleb = [celebs objectAtIndex:0];
        topCelebImageView.image = [[UIImage alloc] initWithData:topCeleb.imageData];
        NSLog(@"loadCelebrities topCeleb: %@", [topCeleb description]);
        celebrityLabel.text = [NSString stringWithFormat:@"%@ %@", topCeleb.firstName, topCeleb.lastName];
        selectedLabel.text = [topCeleb.selectedCount stringValue];
        rejectedLabel.text = [topCeleb.rejectedCount stringValue];
    }
}

-(void) getStats {
    if(![[FatFractal main] loggedIn]) {
        [(AppDelegate *)[[UIApplication sharedApplication] delegate] 
         showLoginWithDelegate:self action:@selector(getStats) 
         message:@"Please Login"];
    }
    else {
        NSError *error;
        statsObject= [[FatFractal main] getObjFromUrl:@"/ff/ext/Stats" error:&error];
        if (error) {
            NSLog(@"StatsViewController getStats failed: %@", [error localizedDescription]);
            return;
        } else {
            NSLog(@"loadCelebrities statsObject: %@", [statsObject description]);
            totalUsersLabel.text = [statsObject.totalUsers stringValue];
            totalCelebritiesLabel.text = [statsObject.totalCelebrities stringValue];
            totalRatingsLabel.text = [statsObject.totalRatings stringValue];
            yourRatingsLabel.text = [statsObject.yourRatings stringValue];
        }
    }
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    topCeleb = [[Celebrity alloc]init];
    statsObject = [[StatsObject alloc]init];
    celebrityLabel.text = nil;
    selectedLabel.text = nil;
    rejectedLabel.text = nil;
    totalRatingsLabel.text = nil;
    totalCelebritiesLabel.text = nil;
    totalUsersLabel.text = nil;
    yourRatingsLabel.text = nil;
    topCelebImageView.image = nil;
    [[topCelebImageView layer] setCornerRadius:16.0f];
    [[topCelebImageView layer] setMasksToBounds:YES];    
    [[topCelebImageView layer] setBorderColor:[UIColor greenColor].CGColor];
    [[topCelebImageView layer] setBorderWidth:4.0f];
    topCelebImageView.image = [UIImage imageNamed:@"mystery3.png"];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self getTopCelebrity];
    [self getStats];
    
}

- (void)viewWillDisappear:(BOOL)animated
{
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
