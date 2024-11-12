import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppAnalyticsComponent } from './app-analytics.component';

describe('AppAnalyticsComponent', () => {
  let component: AppAnalyticsComponent;
  let fixture: ComponentFixture<AppAnalyticsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AppAnalyticsComponent]
    });
    fixture = TestBed.createComponent(AppAnalyticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
