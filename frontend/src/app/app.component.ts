import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

interface CheckIn {
  sleep: number;
  nutrition: number;
  stress: number;
  activity: number;
}

interface Resource {
  label: string;
  url: string;
  priority: boolean;
  reason?: string;
}

interface CheckInResponse {
  ok: boolean;
  message: string;
  resources: Resource[];
}

interface Stats {
  count: number;
  avgSleep: number | null;
  avgNutrition: number | null;
  avgStress: number | null;
  avgActivity: number | null;
  communityPulse: number;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  private apiUrl = 'http://localhost:8080/api';

  // Form data
  checkIn: CheckIn = {
    sleep: 5,
    nutrition: 5,
    stress: 5,
    activity: 5
  };

  // UI state
  isSubmitting = false;
  showSuccess = false;
  successMessage = '';
  errorMessage = '';
  showAdminButton = false;

  // Response data
  resources: Resource[] = [];
  stats: Stats = {
    count: 0,
    avgSleep: null,
    avgNutrition: null,
    avgStress: null,
    avgActivity: null,
    communityPulse: 50
  };

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // Check if admin mode enabled
    const urlParams = new URLSearchParams(window.location.search);
    this.showAdminButton = urlParams.get('admin') === 'true';

    // Load initial stats
    this.loadStats();
  }

  submitCheckIn() {
    if (this.isSubmitting) return;

    this.isSubmitting = true;
    this.errorMessage = '';
    this.showSuccess = false;

    // FIXED: Changed from /checkins to /checkin to match backend
    this.http.post<CheckInResponse>(`${this.apiUrl}/checkin`, this.checkIn)
      .subscribe({
        next: (response) => {
          if (response.ok) {
            this.resources = response.resources;
            this.showSuccess = true;
            this.successMessage = response.message;

            // Auto-fade success message after 5 seconds
            setTimeout(() => {
              this.showSuccess = false;
            }, 5000);

            // Auto-refresh stats
            this.loadStats();
          } else {
            this.errorMessage = response.message;
          }
          this.isSubmitting = false;
        },
        error: (err) => {
          this.errorMessage = 'Unable to connect. Please try again.';
          this.isSubmitting = false;
        }
      });
  }

  loadStats() {
    this.http.get<Stats>(`${this.apiUrl}/stats`)
      .subscribe({
        next: (data) => {
          this.stats = data;
        },
        error: (err) => {
          console.error('Failed to load stats:', err);
        }
      });
  }

  seedData() {
    this.http.post<any>(`${this.apiUrl}/seed`, {})
      .subscribe({
        next: (response) => {
          if (response.ok) {
            this.loadStats();
            alert(response.message);
          }
        },
        error: (err) => {
          alert('Failed to seed data');
        }
      });
  }

  openResource(url: string) {
    window.open(url, '_blank');
  }

  formatAverage(value: number | null): string {
    return value !== null ? value.toFixed(1) : '—';
  }
}
