import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpErrorResponse, provideHttpClient } from '@angular/common/http';
import { of, throwError } from 'rxjs';

import { Authority } from 'app/config/authority.constants';
import { UserManagementService } from '../user-management/service/user-management.service';

import StaffProvisioningComponent from './staff-provisioning.component';

describe('StaffProvisioningComponent', () => {
  let comp: StaffProvisioningComponent;
  let fixture: ComponentFixture<StaffProvisioningComponent>;
  let userService: Pick<UserManagementService, 'create'>;

  beforeEach(waitForAsync(() => {
    userService = {
      create: jest.fn(),
    };

    TestBed.configureTestingModule({
      imports: [StaffProvisioningComponent],
      providers: [provideHttpClient(), { provide: UserManagementService, useValue: userService }],
    })
      .overrideTemplate(StaffProvisioningComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaffProvisioningComponent);
    comp = fixture.componentInstance;
  });

  it('should create a FARMACIST user with proper authority', () => {
    (userService.create as jest.Mock).mockReturnValue(of({}));
    comp.editForm.setValue({
      login: 'farmacist.user',
      firstName: 'Ana',
      lastName: 'Pop',
      email: 'ana.pop@example.com',
      activated: true,
      langKey: 'ro',
      role: 'FARMACIST',
    });

    comp.createUser();

    expect(userService.create).toHaveBeenCalledWith(
      expect.objectContaining({
        login: 'farmacist.user',
        email: 'ana.pop@example.com',
        authorities: [Authority.FARMACIST],
      }),
    );
    expect(comp.successMessage).toContain('FARMACIST');
    expect(comp.isSaving()).toBe(false);
  });

  it('should show backend error details when create fails', () => {
    (userService.create as jest.Mock).mockReturnValue(
      throwError(() => new HttpErrorResponse({ status: 400, error: { detail: 'Email deja folosit' } })),
    );
    comp.editForm.setValue({
      login: 'medic.user',
      firstName: 'Ion',
      lastName: 'Ionescu',
      email: 'ion.ionescu@example.com',
      activated: true,
      langKey: 'ro',
      role: 'MEDIC',
    });

    comp.createUser();

    expect(comp.errorMessage).toBe('Email deja folosit');
    expect(comp.isSaving()).toBe(false);
  });
});
