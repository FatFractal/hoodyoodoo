//
//  TopCelebViewController.h
//  hoodyadoo
//
//  Copyright (c) 2012 FatFractal, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Celebrity.h"
#import "StatsObject.h"

@interface TopCelebViewController : UIViewController

/*! The #celebrity property is the Celebrity to be CRUD Read from the StatsViewController.*/
@property (strong, nonatomic) Celebrity *topCeleb;
/*! The #topCelebImageView property is the UIImageView that displays the image for the TopCelebrity.*/
@property (nonatomic, retain) IBOutlet UIImageView *topCelebImageView;
/*! The #statsObject property is the StatsObject to be CRUD Read from the StatsViewController.*/
@property (strong, nonatomic) StatsObject *statsObject;
/*! The #celebrityLabel property is the UILabel that will display the name of the Celebrity from #celebrity.*/
@property (nonatomic, retain) IBOutlet UILabel *celebrityLabel;
/*! The #selectedLabel property is the UILabel that will display the NSNumber the total selected ratings from #topCeleb.*/
@property (nonatomic, retain) IBOutlet UILabel *selectedLabel;
/*! The #rejectedLabel property is the UILabel that will display the NSNumber the total rejected ratings from #topCeleb.*/
@property (nonatomic, retain) IBOutlet UILabel *rejectedLabel;
/*! The #yourRatingsLabel property is the UILabel that will display the NSNumber the total FFUsers from #statsObject.*/
@property (nonatomic, retain) IBOutlet UILabel *totalUsersLabel;
/*! The #yourRatingsLabel property is the UILabel that will display the NSNumber the total Celebrities from #statsObject.*/
@property (nonatomic, retain) IBOutlet UILabel *totalCelebritiesLabel;
/*! The #yourRatingsLabel property is the UILabel that will display the NSNumber the total ratings from #statsObject.*/
@property (nonatomic, retain) IBOutlet UILabel *totalRatingsLabel;
/*! The #yourRatingsLabel property is the UILabel that will display the NSNumber your total ratings from #statsObject.*/
@property (nonatomic, retain) IBOutlet UILabel *yourRatingsLabel;

@end
