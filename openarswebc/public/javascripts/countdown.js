/**
*
* @Script Countdown
* @version 1.0.0
* @copyright (c) Agrizlive.com
* @license You are allowed to modify this script. But you must provide a link of agrizlive.com in your footer (bottom of the page)
*
* Tested : PHP 5.2.11
*/

/**
*/

jQuery.fn.countDown = function(settings,to) {
	settings = jQuery.extend({
		startFontSize: '15px',
		endFontSize: '15px',
		duration: 1000,
		startNumber: 10,
		dispYear: true,
		dispMonth:  true,
		dispDays: true,
		dispHours: true,
		dispMinutes: true,
		dispSeconds: true,
		dispLeadingZero: true,
		endNumber: 0,
		callBack: function() { }
	}, settings);
	return this.each(function() {
		
		//where do we start?
		if(!to && to != settings.endNumber) { 
			to = settings.startNumber; 
		}

		//loopage
		$(this).animate({
			'fontSize': settings.startFontSize
		},settings.duration,'',function() {
			if(to > settings.endNumber) {
				//Create the timer
				countdownDisp = '';
					if(to < 60)
					{
						year = month = days = hours = minutes = 0; 
						seconds = to;
					}
					else if(to < 3600)
					{
						year = month = days = hours = 0; 
						minutes = parseInt(to/60);
						seconds = parseInt(to % 60);
					}
					else
					{
						if(to < 31536000){year = 0;}else{year = parseInt(to / 31536000 );}
						month = parseInt((to - (31536000 * year)) / 2592000);
						days = parseInt((to - (31536000 * year) - (2592000 * month)) / 86400);
						hours = parseInt((to - (31536000 * year) - (2592000 * month) - (86400 * days)) / 3600);
						minutes = parseInt((to - (31536000 * year) - (2592000 * month) - (86400 * days) - (3600 * hours)) / 60);
						seconds = parseInt(to % 60);
					}
					year = (year < 0 ) ? 0 : year;
					month = (month < 0 ) ? 0 : month;
					days = (days < 0 ) ? 0 : days;
					hours = (hours < 0 ) ? 0 : hours;
					minutes = (minutes < 0 ) ? 0 : minutes;
					seconds = (seconds < 0 ) ? 0 : seconds;
					
						
					if(settings.dispYear || settings.dispMonth || settings.dispDays)
					{	
						if(settings.dispLeadingZero){
							year = ( year <= 9 ) ? '0'+year : year;
							month = ( month <= 9 ) ? '0'+month : month;
							days = ( days <= 9 ) ? '0'+days : days;
							hours = ( hours <= 9 ) ? '0'+hours : hours;
							minutes = ( minutes <= 9 ) ? '0'+minutes : minutes;
							seconds = ( seconds <= 9 ) ? '0'+seconds : seconds;
						}
						
						if(settings.dispYear){
							countdownDisp = year + ' years ';
						}
						if(settings.dispMonth){
							countdownDisp += month + ' months ';
						}
						if(settings.dispDays){
							countdownDisp += days + ' days ';
						}
						if(settings.dispHours){
							countdownDisp += hours + ' hours ';
						}
						if(settings.dispMinutes){
							countdownDisp += minutes + ' minutes ';
						}
						if(settings.dispSeconds){
							countdownDisp += seconds + ' seconds';
						}
						
					}
					else
					{
						if(settings.dispLeadingZero){
							year = ( year <= 9 ) ? '0'+year : year;
							month = ( month <= 9 ) ? '0'+month : month;
							days = ( days <= 9 ) ? '0'+days : days;
							hours = ( hours <= 9 ) ? '0'+hours : hours;
							minutes = ( minutes <= 9 ) ? '0'+minutes : minutes;
							seconds = ( seconds <= 9 ) ? '0'+seconds : seconds;
						}
						
						if(settings.dispYear){
							countdownDisp = year + ':';
						}
						if(settings.dispMonth){
							countdownDisp += month + ':';
						}
						if(settings.dispDays){
							countdownDisp += days + ':';
						}
						if(settings.dispHours){
							countdownDisp += hours + ':';
						}
						if(settings.dispMinutes){
							countdownDisp += minutes + ':';
						}
						if(settings.dispSeconds){
							countdownDisp += seconds;
						}
						
					}
					
				//set the countdown to the starting number
				$(this).text(countdownDisp).css('fontSize',settings.startFontSize);
				$(this).css('fontSize',settings.startFontSize).text(countdownDisp).countDown(settings,to - 1);
				var ts = Math.round(new Date().getTime() / 1000);
			}
			else
			{
				settings.callBack(this);	
			}
		});
				
	});
};